const val zero = 0
fun main() {
    // put your code here
    var total = 0;
    do {
        val input = readln().toInt()
        total += input
    } while (input != zero)
    print(total)
}