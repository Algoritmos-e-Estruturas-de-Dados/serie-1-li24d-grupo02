package parte1

import kotlin.math.absoluteValue

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
            if (diff < minDiff) {
                minDiff = diff
            }
        }
    }
    return minDiff
} // O(n^2)

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
    for (j in 0..<k) { // Initialize beggining sum
        sum += v[j]
    }

    // Update final pair's values according to sum
    if (sum < lower) {
        subarrayAmountLower++
    }
    else if (sum > upper) {
        subarrayAmountUpper++
    }

    // Iterate through array without surpassing (array's last index - k) to not go out of bounds because of summing iteration method
    for (i in 0..v.lastIndex - k) {
        sum -= v[i] // Subtract first element of last subarray
        sum += v[i + k] // Add new element of last subarray

        // Update final pair's values according to sum
        if (sum < lower) {
            subarrayAmountLower++
        }
        else if (sum > upper) {
            subarrayAmountUpper++
        }
    }

    return Pair(subarrayAmountLower, subarrayAmountUpper)
} // O(n)

