“This is a development done to satisfy the assignment portion for module “Mobile App development” under Ngee Ann Polytechnic for AY24/25”.
Jayden Toh Xuan Ming, S10241868J

Wack a Mole

Tools Used: Gemini
Basic Section

Problem #1:
The issue was that the M randomiser was constantly moving despite the
game not starting, and I did not know what to do to fix it. I also 
have issue in regards that the game continues to reset itself when I
click restart, not stop the game after restart and start from there. What
should I do to fix this?

Prompt:
The issue with my current wack a mole game here is that the M was constantly moving actively even 
when the game has not started. I do not know what is causing this. When I click restart, the game
also constantly resets and continue playing which should not be the case, where restarting means
ending the game interaction.

Before:
```kotlin
Button(
    onClick = { isPlaying = true },
    modifier = Modifier
        .padding(16.dp)
        .align(Alignment.CenterHorizontally)
) {
    Text(if (isPlaying) "Restart" else "Start")
}
``` 

```kotlin
After:
Button(
    onClick = {
        if (isPlaying) {
            isPlaying = false
            timeLeft = 30
            score = 0
        } else {
            isPlaying = true
            restart++ 
        }
    },
    modifier = Modifier
        .padding(16.dp)
        .align(Alignment.CenterHorizontally)
) {
    Text(if (isPlaying) "Restart" else "Start")
}
``` 

Problem #2
There was an error for trying to solve game over displaying final score and a screen to show. 
Dont know how exactly to use alertdialog and what was missing. how to fix. 

Prompt
I recieved an error when trying to solve the game and displaying the final score with a display
to show. I also do not know how to use AlertDialog properly, can you explain clearly how this
can be used properly and how to fix this issue.

Before:
```kotlin
if (gameOver) {
    AlertDialog(
        title = { Text(text = "Game Over") },
        text = { Text(text = "Your final score was: $score") },
        confirmButton = {
            TextButton(
                onClick = { gameOver = false }
            ) {
                Text("Ok")
            }
        }
    )
}
```

After:
```kotlin
if (gameOver) {
    AlertDialog(
        onDismissRequest = { gameOver = false },
        title = { Text(text = "Game Over") },
        text = { Text(text = "Your final score was: $score") },
        confirmButton = {
            TextButton(
                onClick = { gameOver = false }
            ) {
                Text("Ok")
            }
        }
    )
}
``` 

Advanced Section

Problem #3
I need to support multi user login by checking if a username and password
exist in the database, but I only know the basics due to lack of experience 
in using room.

Prompt
I need to check if the database if the username and the password match. I am
unsure how to write a query to do it, please advice how I can continue from here 
with proper explanation.

Before:
```kotlin
@Dao
interface AppDao {
    @Insert
    suspend fun insertUser(user: User)
    
    @Insert
    suspend fun insertScore(score: Score)
}
```

After:
```kotlin
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
}
``` 

Problem #4
I did not know how to easily hash the password without making a hassle in the room database. I wanted
to find a code in android studio that can hash it fast. 

Prompt
I need to hash the password for a login/signup screen, but I do not want a long way. Do you know a way 
that can allow one to easily hash the password when the user is typing?

Before:
```kotlin
OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text(text = "Password") },
)
```

After:
```kotlin
OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text(text = "Password") },
    visualTransformation = PasswordVisualTransformation()
)
```

Key Takeaways learnt in each of these areas stated
- I learned how to properly use state variables like restart++ as a key in LaunchedEffect to force the game
to restart.
- I learned that AlertDialog was controlled by the state and how to use it. They are used within conditions.
- I learned about a new jetpack compose visualtransformation that can be used to hash password and manipulate how text would look like without touching the database
- I learned that room database is not limited to basic CRUD operations but even SQL queries to perform logic. This can enable one to better expand the reach of using such database for coding. 
