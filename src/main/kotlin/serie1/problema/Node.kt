package serie1.problema

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
