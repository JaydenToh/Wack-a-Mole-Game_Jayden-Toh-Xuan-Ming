package com.example.wackamole

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val password: String
)

@Entity(
    tableName = "scores",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val score: Int,
    val timestamp: Long
)

@Dao
interface AppDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM user_table WHERE username = :name AND password = :pass LIMIT 1")
    suspend fun login(name: String, pass: String): User?

    @Query("SELECT * FROM user_table WHERE username = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?

    @Insert
    suspend fun insertScore(score: Score)

    @Query("""
        SELECT u.username, MAX(s.score) as maxScore 
        FROM user_table u 
        JOIN scores s ON u.userId = s.userId 
        GROUP BY u.userId 
        ORDER BY maxScore DESC
    """)
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
}

data class LeaderboardEntry(
    val username: String,
    val maxScore: Int
)

@Database(entities = [User::class, Score::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun dao(): AppDao
    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                    .build().also { INSTANCE = it }
            }
        }
    }
}