package serie1.problema

import java.util.PriorityQueue

/**
 * Performs external sorting on a large input file making use of libraries.
 *
 * Steps:
 * 1. Splits the input file into smaller sorted partitions using `createSortedPartitions`.
 * 2. Initializes a priority queue (min-heap) with the smallest element from each partition file.
 * 3. Repeatedly extracts the minimum value from the queue and writes it to the output.
 *    - After removing the smallest element, reads the next value from the same file and inserts it into the queue.
 * 4. Writes the fully sorted sequence to the specified output file.
 *
 * Parameters:
 * - args[0]: Path to the input file to be sorted.
 * - args[1]: Path to the output file where sorted data will be written.
 * - args[2]: Partition size, i.e., number of elements per sorted chunk.
 */
fun main(args: Array<String>) {
    val inputFilename = args[0]
    val outputFilename = args[1]
    val partitionSize = args[2].toInt()

    val numWays = createSortedPartitions(inputFilename, partitionSize) // Create sorted partition files
    val readers = Array(numWays) { i -> createReader("${i + 1}.txt") } // One reader per partition file

    // PriorityQueue used as a min-heap based on Node's value
    val heap = PriorityQueue<Node>(compareBy { it.value })

    // Initialize heap with the first element of each file
    for (i in 0 until numWays) {
        val line = readers[i].readLine()
        val value = line.toInt()
        heap.add(Node(i, value))
    }

    val writer = createWriter(outputFilename)

    while (heap.isNotEmpty()) {
        val node = heap.poll() // Extract the minimum value node
        writer.println(node.value)  // Write it to the sorted output

        val nextLine = readers[node.fileIndex].readLine()

        if (nextLine == null) continue

        val nextValue = nextLine.toInt()
        heap.add(Node(node.fileIndex, nextValue)) // Add next value from the same file
    }

    // Close resources
    readers.forEach { it.close() }
    writer.close()
}
