“This is a development done to satisfy the assignment portion for module “Mobile App development” under Ngee Ann Polytechnic for AY24/25”.
Jayden Toh Xuan Ming, S10241868J

Before:
    LaunchedEffect(isPlaying) {
        while (timeLeft > 0) {
            delay((700..1000).random())
            currentMoleIndex = (0..8).random()
        }
    }

Problem:
I was not sure what to do as the delay had an error. 

Prompt:
I got this error as you can see in the screenshot I inserted. Please explain what this error
means and what I should add to the line of code to solve it.

After:
LaunchedEffect(isPlaying) {
    while (timeLeft > 0) {
        delay((700..1000).random().toLong())
        currentMoleIndex = (0..8).random()
    }
}

Before:
Button(
onClick = {
isPlaying = true},
modifier = Modifier
.padding(16.dp)
.align(Alignment.CenterHorizontally)
) {
Text(if (isPlaying) "Restart" else "Start")
}

The issue was that the M randomiser was constantly moving despite the
game not starting, and I did not know what to do to fix it. I also 
have issue in regards that the game continues to reset itself when I
click restart, not stop the game after restart and start from there. What
should I do to fix this?

Prompt


After
Button(
onClick = {
isPlaying = true
restart++ },
modifier = Modifier
.padding(16.dp)
.align(Alignment.CenterHorizontally)
) {
Text(if (isPlaying) "Restart" else "Start")
}