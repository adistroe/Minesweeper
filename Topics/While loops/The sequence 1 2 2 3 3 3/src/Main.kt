fun main() {
    // put your code here
    val seqSize = readln().toInt()
    var currSize = 0
    var numToPrint = 1
    do {
        repeat(if (numToPrint < seqSize - currSize) numToPrint else seqSize - currSize) {
            print("$numToPrint ")
            currSize++
        }
        numToPrint++
    } while (currSize < seqSize)
}