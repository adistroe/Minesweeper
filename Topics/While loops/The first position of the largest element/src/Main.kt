import java.util.Scanner

fun main() {
    // put your code here
    var max = readln().toInt()
    var position = 1
    var counter = 1
    val scanner = Scanner(System.`in`)

    while (scanner.hasNextInt()) {
        val num = scanner.nextInt()
        counter++
        if (num > max) {
            max = num
            position = counter
        }
    }

    println("$max $position")
}