package com.example.wackamole

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wackamole.ui.theme.WackAMoleTheme
import kotlinx.coroutines.launch

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
    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.dao()
    var currentUserId by remember { mutableStateOf<Int?>(null) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                dao = dao,
                onLoginSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate("game") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("game") {
                if (currentUserId != null) {
                    GameScreen(
                        userId = currentUserId!!,
                        dao = dao,
                        onNavigateToHighScore = { navController.navigate("highscore") }
                    )
                }
        }

        composable("highscore") {
            HighScoreScreen(
                dao = dao,
                onNavigateBack = { navController.popBackStack()}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    userId: Int,
    dao: AppDao,
    onNavigateToHighScore: () -> Unit) {

    var scope = rememberCoroutineScope()
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var currentMoleIndex by remember {mutableStateOf(-1) }
    var isPlaying by remember { mutableStateOf(false) }
    var restart by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    LaunchedEffect(restart, isPlaying) {
        if (isPlaying) {
            score = 0
            timeLeft = 30
            currentMoleIndex = -1
            gameOver = false

            while (timeLeft > 0 && isPlaying) {
                delay(1000)
                timeLeft = timeLeft - 1
            }
            if (isPlaying) {
                isPlaying = false
                currentMoleIndex = -1
                gameOver = true
            }

            if (gameOver) {
                scope.launch {
                    dao.insertScore(
                        Score(userId = userId, score = score, timestamp = System.currentTimeMillis())
                    )
                }
            }
        }
    }

    LaunchedEffect(restart) {
        if (isPlaying) {
            while (timeLeft > 0 && isPlaying) {
                delay((700..1000).random().toLong())
                currentMoleIndex = (0..8).random()
            }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
       TopAppBar(
           title = { Text("Wack a Mole") },
           actions = {
               IconButton(onClick = { onNavigateToHighScore() }) {
                   Icon(
                       Icons.Default.Leaderboard, contentDescription = "High Score")
               }
           }
       )
        Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
       ) {
            Text(
                text = "Score: $score",
                fontSize = 22.sp
            )
            Text(
                text = "Time: $timeLeft",
                fontSize = 22.sp
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items (9) { index ->
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f),
                    onClick = {
                        if (isPlaying && index == currentMoleIndex)
                            score++
                        currentMoleIndex = -1
                    }
                ) {
                    if (isPlaying && index == currentMoleIndex) {
                        Text("M")
                    }
                }
            }
        }

        Button(
            onClick = {
                if (isPlaying) {
                    isPlaying = false
                    timeLeft = 30
                    score = 0
                    currentMoleIndex = -1
                } else {
                    isPlaying = true
                    restart++
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                if (isPlaying) "Restart" else "Start",
                fontSize = 25.sp
            )
        }

        if (gameOver) {
            AlertDialog(
                onDismissRequest = {gameOver = false},
                title = {
                    Text(
                        text = "Game Over",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Your final score was: $score",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                        ) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            gameOver = false
                        }
                    ) {
                        Text(
                            "Ok",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighScoreScreen(
    dao: AppDao,
    onNavigateBack: () -> Unit) {

    val leaderboard by dao.getLeaderboard().collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Leaderboard") },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("User")
            Text("Best Score")
        }

        androidx.compose.foundation.lazy.LazyColumn {
            items(leaderboard.size) { index ->
                val entry = leaderboard[index]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(entry.username)
                    Text(entry.maxScore.toString())
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    dao: AppDao,
    onLoginSuccess: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var username by rememberSaveable { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Login & Sign Up Page",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = { Text(text = "Username")},
            )

            Spacer(modifier = Modifier.padding(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                onClick = {
                    scope.launch {
                        val user = dao.login(username, password)
                        if (user != null) {
                            onLoginSuccess(user.userId)
                        } else {
                            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ) {
                Text(
                    text = "Login",
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        scope.launch {
                            val existing = dao.getUserByName(username)
                            if (existing == null) {

                                val newUser = User(username = username, password = password)
                                dao.insertUser(newUser)

                                Toast.makeText(context, "Account Created! Please Login.", Toast.LENGTH_SHORT).show()

                                username = ""
                                password = ""

                            } else {
                                Toast.makeText(context, "Username taken", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Enter details", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp
                )
            }
        }
    }
}