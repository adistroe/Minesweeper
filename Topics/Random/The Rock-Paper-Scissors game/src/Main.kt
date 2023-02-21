import kotlin.random.Random
import kotlin.random.nextInt

fun makeDecision(): String {
    // write here
    return when (Random.nextInt(1..3)) {
        1 -> "Rock"
        2 -> "Paper"
        else -> "Scissors"
    }
}