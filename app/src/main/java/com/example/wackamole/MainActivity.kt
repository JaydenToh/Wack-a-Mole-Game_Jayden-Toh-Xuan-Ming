package com.example.wackamole

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.navigation.compose.rememberNavController
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

