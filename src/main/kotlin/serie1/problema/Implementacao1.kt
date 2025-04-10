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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*****************                              HEAP IMPLEMENTATION                                     *****************/

/**
 * Represents a node in the heap structure.
 *
 * @property fileIndex The index of the file this value originated from (used for tracking in multiway merge).
 * @property value The integer value held by this node.
 */
data class Node(val fileIndex: Int, val value: Int)


/**
 * Inserts a [Node] into the list while maintaining min-heap properties.
 *
 * @receiver A mutable list of [Node]s that functions as a min-heap.
 * @param newNode The new node to insert into the heap.
 */
fun MutableList<Node>.insert(newNode: Node) {
    add(newNode)   // Append new node

    // Swap inserted node with parent node while parent is bigger
    var currentIndex = lastIndex
    while (true) {
        val parentIndex = (currentIndex - 1) / 2
        if (this[currentIndex].value >= this[parentIndex].value)
            break
        swap(currentIndex, parentIndex)
        currentIndex = parentIndex
    }
}


/**
 * Removes and returns the smallest [Node] from the min-heap while preserving heap structure.
 *
 * @receiver A mutable list of [Node]s functioning as a min-heap.
 * @return The node with the smallest value in the heap.
 */
fun MutableList<Node>.popMin(): Node {
    swap(0, lastIndex)
    val min = removeLast()

    var currentIndex = 0
    while (true) {
        val leftIndex = currentIndex * 2 + 1
        val rightIndex = currentIndex * 2 + 2

        // No children — done
        if (leftIndex > lastIndex) break

        // Only left child
        if (rightIndex > lastIndex) {
            if (this[currentIndex].value > this[leftIndex].value)
                swap(currentIndex, leftIndex)
            break
        }

        // Both children exist — find smaller one
        val switchIndex = if (this[leftIndex].value <= this[rightIndex].value) leftIndex else rightIndex

        if (this[currentIndex].value > this[switchIndex].value)
            swap(currentIndex, switchIndex)
        else
            break

        currentIndex = switchIndex
    }

    return min
}


/**
 * Swaps two elements in the list at the specified indices.
 *
 * @receiver A mutable list of [Node]s.
 * @param i The index of the first element to swap.
 * @param j The index of the second element to swap.
 */
fun MutableList<Node>.swap(i: Int, j: Int) {
    this[i] = this[j].also { this[j] = this[i] }
}
