package parte1

import kotlin.math.absoluteValue
import kotlin.math.ceil

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 1.1

/**
 * Computes the minimum absolute difference between any pair of elements
 * from two given integer arrays.
 *
 * This function iterates through all possible pairs of elements, one from
 * each array, and calculates their absolute differences. It returns the
 * smallest absolute difference found. If either of the arrays is empty,
 * the function returns -1.
 *
 * @param elem1 The first array of integers.
 * @param elem2 The second array of integers.
 * @return The minimum absolute difference between any pair of elements
 *         from the two arrays, or -1 if either array is empty.
 */
fun findMinDifference(elem1: IntArray, elem2: IntArray): Int {
    // If any of the arrays are empty
    if (elem1.isEmpty() or elem2.isEmpty())
        return -1

    // Initialize minDiff to the absolute value of the first element of each array to achieve desired initialization
    var minDiff: Int = (elem1[0] - elem2[0]).absoluteValue

    // Nested loop to match every element of each array to eachother
    for (i in elem1.indices) {
        for (j in elem2.indices) {
            val diff = (elem1[i] - elem2[j]).absoluteValue // Formula for absolute difference

            // Update minimum absolute difference to curent difference if the current one is closer to 0 than than the minimum
            if (diff < minDiff) 
                minDiff = diff
        }
    }
    return minDiff
}   // O(n^2)

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 1.2

/**
 * Counts the number of contiguous subarrays of length `k` whose sums
 * fall below a given `lower` bound or exceed an `upper` bound.
 *
 * The function computes the sum of the first `k` elements and then
 * iterates through the array using a sliding window approach. At each
 * step, it updates the sum by subtracting the outgoing element and
 * adding the incoming element. It keeps track of how many subarray
 * sums are below `lower` and how many exceed `upper`.
 *
 * @param v The input array of integers.
 * @param k The size of the subarrays to consider.
 * @param lower The lower bound for the subarray sum.
 * @param upper The upper bound for the subarray sum.
 * @return A pair where the first value is the count of subarrays
 *         with sums below `lower`, and the second value is the count
 *         of subarrays with sums above `upper`.
 */
fun counter(v: IntArray, k: Int, lower: Int, upper: Int): Pair<Int, Int> {
    var subarrayAmountLower = 0
    var subarrayAmountUpper = 0

    var sum = 0
    for (j in 0..<k)   // Initialize beggining sum
        sum += v[j]
    

    // Update final pair's values according to sum
    if (sum < lower)
        subarrayAmountLower++
    
    else if (sum > upper)
        subarrayAmountUpper++
    

    // Iterate through array without surpassing (array's last index - k) to not go out of bounds because of summing iteration method
    for (i in 0..v.lastIndex - k) {
        sum -= v[i] // Subtract first element of last subarray
        sum += v[i + k] // Add new element of last subarray

        // Update final pair's values according to sum
        if (sum < lower)
            subarrayAmountLower++
        
        else if (sum > upper)
            subarrayAmountUpper++
    }

    return Pair(subarrayAmountLower, subarrayAmountUpper)
}   // O(n)

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 1.3

/**
 * Retrieves the total number of lines (integers) in a file.
 *
 * This function opens the specified file, reads through each line, and counts the total number of lines (integers).
 * It is useful for determining the size of the file before processing its content in chunks.
 *
 * @param fileName The name of the file to read.
 * @return The total number of integers (lines) in the file.
 */
fun getFileSize(fileName: String): Int {
    val reader = createReader(fileName)
    var count = 0
    while (reader.readLine() != null)
        count++
    reader.close()
    return count
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
            numbers[i] = line.toInt()
        }
    return numbers
}

/**
 * Sorts this partition using Merge Sort and writes the sorted result to a file.
 *
 * This function sorts the calling [IntArray] in ascending order using [mergeSort],
 * then writes the sorted numbers to a new file named `"<fileIndex>.txt"`.
 *
 * @receiver The integer array (partition) to be sorted and written to a file.
 * @param fileIndex The index used to name the output file.
 */
fun IntArray.processPartition(fileIndex: Int) {
    mergeSort()
    val writer = createWriter("$fileIndex.txt")
    for (num in this)
        writer.println(num)
    writer.close()
}


/**
 * Splits a large file of integers into multiple sorted partitions.
 *
 * This function reads a file containing integers, splits it into smaller partitions of at most
 * [partitionSize] elements, sorts each partition, and writes them to separate files.
 * The number of partitions created is returned.
 *
 * @param fileName The name of the file containing the integers.
 * @param partitionSize The maximum number of integers per partition.
 * @return The total number of sorted partitions created.
 *
 * @throws IllegalArgumentException If the file is empty, if [partitionSize] is larger than the total number of integers,
 * or if [partitionSize] is too large to create at least one partition.
 */
fun createSortedPartitions(fileName: String, partitionSize: Int): Int {
    val n = getFileSize(fileName) // First pass to get 'n'
    require(n > 0)  {"The file needs to have at least one number"}
    require(partitionSize <= n) {"'partitionSize' needs to be smaller than 'n' (amount of numbers in the file)"}

    val numWays = ceil(n.toDouble() / partitionSize).toInt() // Total number of files
    require(numWays > 0)    {"'partitionSize' is too big compared to 'n'"}

    val reader = createReader(fileName)

    for (i in 1 until numWays) {
        val numbers = reader.readIntChunk(partitionSize)  // Read chunk into IntArray
        numbers.processPartition(i)   // Sort and write chunk into file
    }

    // Diferent reading for the last file because of the rest amount of numbers to read
    val numbers = reader.readIntChunk(n - (numWays - 1) * partitionSize)
    numbers.processPartition(numWays)

    reader.close()
    return numWays
}   // O(nlog(n))


/**
 * Sorts this [IntArray] in ascending order using the Merge Sort algorithm.
 *
 * This function applies the iterative bottom-up merge sort technique,
 * which has a time complexity of O(n log n).
 * The sorting is performed in place, modifying the original array.
 *
 * @receiver The integer array to be sorted.
 */
fun IntArray.mergeSort() = mergeSortHelper()


/**
 * Iteratively sorts this [IntArray] using Merge Sort.
 *
 * This function employs a bottom-up approach, progressively merging subarrays of increasing size
 * until the entire array is sorted.
 *
 * @receiver The integer array being sorted.
 */
fun IntArray.mergeSortHelper() {
    val n = this.size
    var width = 1  // Width of the subarrays to be merged

    // Double the width each iteration until it covers the entire array
    while (width < n) {
        var i = 0
        while (i < n - width) {
            val mid = i + width - 1
            val rightEnd = minOf(i + 2 * width - 1, n - 1)

            // Merge two adjacent subarrays of size `width`
            merge(i, mid, rightEnd)

            i += 2 * width  // Move to the next pair of subarrays
        }
        width *= 2  // Increase subarray size for the next iteration
    }
}


/**
 * Merges two sorted subarrays of this [IntArray] into a single sorted sequence.
 *
 * The subarrays are defined by the indices [l] (left boundary), [mid] (middle point), and [r] (right boundary).
 * The first subarray spans from index `l` to `mid`, and the second subarray spans from `mid + 1` to `r`.
 *
 * This function modifies the original array in place by merging the two sorted halves.
 *
 * @receiver The integer array containing the two sorted subarrays to merge.
 * @param l The starting index of the first sorted subarray.
 * @param mid The ending index of the first sorted subarray, and midpoint of the full range.
 * @param r The ending index of the second sorted subarray.
 */
fun IntArray.merge(l: Int, mid: Int, r: Int) {
    val left = sliceArray(l..mid)       // Left half
    val right = sliceArray(mid + 1..r)  // Right half

    var i = 0  // Left half index
    var j = 0  // Right half index
    var k = l  // Main array index

    while (i < left.size && j < right.size) {
        if (left[i] <= right[j]) {
            this[k] = left[i]  // Take from left
            i++
        } else {
            this[k] = right[j]  // Take from right
            j++
        }
        k++
    }

    // Copy remaining elements
    while (i < left.size) this[k++] = left[i++]
    while (j < right.size) this[k++] = right[j++]
}
