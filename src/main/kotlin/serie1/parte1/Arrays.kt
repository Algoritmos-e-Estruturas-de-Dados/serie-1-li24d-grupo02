package parte1

import kotlin.math.absoluteValue

fun findMinDifference(elem1: IntArray, elem2: IntArray): Int {
    if (elem1.isEmpty() or elem2.isEmpty()) return -1
    var i = 0
    var j = 0
    var diff: Int = (elem1[0] - elem2[0]).absoluteValue
    while (i < elem1.size && j < elem2.size) {
        if(diff > (elem1[i] - elem2[j]).absoluteValue) diff = (elem1[i] - elem2[j]).absoluteValue
        if (elem1[i] < elem2[j]) i++
        else j++
    }
    return diff
}

fun counter(array: IntArray, k: Int, lower: Int, upper: Int): Pair<Int, Int> {
    throw UnsupportedOperationException()
}

