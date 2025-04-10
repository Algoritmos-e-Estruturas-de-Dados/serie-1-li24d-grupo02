package serie1.problema

const val FILE_NAME = "something.txt"
const val PARTITION_SIZE = 100 // some number...

/**
 * Main function to perform external sorting:
 * 1. Divides a large input file into sorted partitions using createSortedPartitions.
 * 2. Performs a k-way merge on the sorted partitions using a min-heap.
 * 3. Writes the fully sorted result back into the original file.
 *
 * Assumes:
 * - FILE_NAME is the path to the input file to be sorted.
 * - PARTITION_SIZE defines how many numbers go into each sorted partition.
 * - createReader, createWriter, insert (min-heap), popMin, and Node are defined elsewhere.
 */
fun main() {
    val numWays = createSortedPartitions(FILE_NAME, PARTITION_SIZE) // Divide main file into numWays sorted partition files
    val indexes = IntArray(numWays) { 1 } // Keeps track of how many lines were read from each file
    val readers = Array(numWays) { i -> createReader("${i + 1}.txt") } // One reader for each partition file
    val heap = mutableListOf<Node>() // Min-heap that stores the current smallest values from each file

    // Fill heap with the smallest number from each file
    for (i in 0 until numWays) {
        val line = readers[i].readLine()
        if (line != null) {
            val value = line.toInt()
            val newNode = Node(i, value)
            heap.insert(newNode)
        }
    }

    val writer = createWriter(FILE_NAME) // Output writer to overwrite the original file with sorted values

    while (heap.isNotEmpty()) {
        val node = heap.popMin() // Get the current smallest number from all files
        writer.println(node.value) // Write it to the sorted output

        val nextLine = readers[node.fileIndex].readLine() // Read the next number from the same file

        if (nextLine == null) continue // Don't insert into heap if there is no next line

        val nextValue = nextLine.toInt()
        heap.insert(Node(node.fileIndex, nextValue)) // Insert the next number from that file into the heap
        indexes[node.fileIndex]++ // Update the index tracker
    }

    // Close all file readers
    for (reader in readers) {
        reader.close()
    }

    writer.close() // Close output writer
}
