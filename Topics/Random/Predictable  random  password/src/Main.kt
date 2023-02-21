import kotlin.random.Random
import kotlin.random.nextInt

fun generatePredictablePassword(seed: Int): String {
    var randomPassword = ""
    // write your code here
    val rnd = Random(seed)
    val range = 33..126
    for (i in 1..10) {
        randomPassword += rnd.nextInt(range).toChar()
    }
    return randomPassword
}