package serie1.problema

import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter


fun createReader(fileName: String): BufferedReader {
    return BufferedReader(FileReader(fileName))
}

fun createWriter(fileName: String): PrintWriter {
    return PrintWriter(fileName)
}

/**
 * Reads a chunk of integers from a [BufferedReader] and returns them as an [IntArray].
 *
 * This function reads [chunkSize] lines from the [BufferedReader], converts each line to an integer,
 * and returns an array of those integers.
 *
 * @receiver The [BufferedReader] from which the integers will be read.
 * @param chunkSize The number of integers (lines) to read.
 * @return An [IntArray] containing the integers read from the file.
 */
fun BufferedReader.readIntChunk(chunkSize: Int): IntArray {
    val numbers = IntArray(chunkSize)
    for (i in 0 until chunkSize) {
        val line = readLine()
        numbers[i] = line!!.toInt()
    }
    return numbers
}

/** Usage Example
 *  Files "input.txt" and "output.txt" are on the project Directory.
 *  This program copies the Input File to OutputFile.
 */
fun main() {
    val br = createReader("input.txt")
    val pw = createWriter("output.txt")
    var line: String?
    line = br.readLine()
    while (line != null) {
        pw.println(line)
        line = br.readLine()
    }
    // Close files
    br.close()
    pw.close()
}
