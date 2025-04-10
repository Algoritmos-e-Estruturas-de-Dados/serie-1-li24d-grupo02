package serie1.problema

/**
 * Performs external sorting on a large input file.
 *
 * Steps:
 * 1. Splits the input file into smaller sorted partitions using `createSortedPartitions`.
 * 2. Initializes a min-heap with the smallest element from each partition file.
 * 3. Repeatedly extracts the minimum value from the heap and writes it to the output.
 *    - After removing the smallest element, reads the next value from the same file and inserts it into the heap.
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

    val numWays = createSortedPartitions(inputFilename, partitionSize) // Divide main file into numWays sorted partition files
    val readers = Array(numWays) { i -> createReader("${i + 1}.txt") } // One reader for each partition file
    val heap = mutableListOf<Node>() // Min-heap that stores the current smallest values from each file

    // Fill heap with the smallest number from each file
    for (i in 0 until numWays) {
        val line = readers[i].readLine()
        val value = line.toInt()
        val newNode = Node(i, value)
        heap.insert(newNode)
    }

    val writer = createWriter(outputFilename)

    while (heap.isNotEmpty()) {
        val node = heap.popMin() // Get the current smallest number from all files
        writer.println(node.value) // Write it to the sorted output

        val nextLine = readers[node.fileIndex].readLine() // Read the next number from the same file

        if (nextLine == null) continue // Don't insert into heap if there is no next line

        val nextValue = nextLine.toInt()
        heap.insert(Node(node.fileIndex, nextValue)) // Insert the next number from that file into the heap
    }

    // Close all file readers
    readers.forEach { it.close() }

    writer.close() // Close output writer
}
