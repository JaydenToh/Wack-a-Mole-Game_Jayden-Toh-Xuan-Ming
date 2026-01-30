package com.example.wackamole

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.delay
import com.example.wackamole.ui.theme.WackAMoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WackAMoleTheme {
                WhackAMoleApp()
            }
        }
    }
}

@Composable
fun WhackAMoleApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "game") {
        composable("game") {
            GameScreen(
                onNavigateToHighScore = { navController.navigate("highscore")}
            )
        }

        composable("highscore") {
            HighScoreScreen(
                onNavigateBack = { navController.popBackStack()}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onNavigateToHighScore: () -> Unit) {
    // Saving the high score data even when app closes
    val context = LocalContext.current
    val SharedPreferences= remember {
        context.getSharedPreferences("score_save", Context.MODE_PRIVATE)
    }

    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var currentMoleIndex by remember {mutableStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }

    var highScore by remember {
        mutableStateOf(SharedPreferences.getInt("high_score",0))
    }

    LaunchedEffect(isPlaying) {
        score = 0
        timeLeft = 30

        while (timeLeft > 0) {
            delay(1000)
            timeLeft = timeLeft - 1
        }

        isPlaying = false
        currentMoleIndex = -1

        if (score > highScore) {
            highScore = score
            SharedPreferences.edit().putInt("high_score", highScore).apply()
        }
    }

    LaunchedEffect(isPlaying) {
        while (timeLeft > 0) {
            delay((700..1000).random().toLong())
            currentMoleIndex = (0..8).random()
        }
    }
}

@Composable
fun HighScoreScreen(onNavigateBack: () -> Unit) {

}