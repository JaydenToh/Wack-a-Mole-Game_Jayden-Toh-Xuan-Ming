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