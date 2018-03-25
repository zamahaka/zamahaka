package laba1

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

var numbers = randoms.toIntArray()

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis { println(sort(numbers.copyOf())) }
    println("rec time: $time")

    println("suspend time: ${measureTimeMillis { println(sortSuspend(numbers.copyOf())) }}")
}

fun sort(array: IntArray) = quickSort(array, 0, array.size - 1)

suspend fun sortSuspend(array: IntArray) = quickSortSuspend(array, 0, array.size - 1)

private fun quickSort(array: IntArray, low: Int, high: Int) {
    var i = low
    var j = high
    // Get the pivot element from the middle of the list
    val pivot = array[low + (high - low) / 2]

    // Divide into two lists
    while (i <= j) {
        // If the current value from the left list is smaller than the pivot
        // element then get the next element from the left list
        while (array[i] < pivot) {
            i++
        }
        // If the current value from the right list is larger than the pivot
        // element then get the next element from the right list
        while (array[j] > pivot) {
            j--
        }

        // If we have found a value in the left list which is larger than
        // the pivot element and if we have found a value in the right list
        // which is smaller than the pivot element then we exchange the
        // values.
        // As we are done we can increase i and j
        if (i <= j) {
            exchange(i, j)
            i++
            j--
        }
    }
    // Recursion
    if (low < j)
        quickSort(array, low, j)
    if (i < high)
        quickSort(array, i, high)
}

private suspend fun quickSortSuspend(array: IntArray, low: Int, high: Int) {
    var i = low
    var j = high
    val length = high - low + 1
    // Get the pivot element from the middle of the list
    val pivot = array[low + (high - low) / 2]

    // Divide into two lists
    while (i <= j) {
        // If the current value from the left list is smaller than the pivot
        // element then get the next element from the left list
        while (array[i] < pivot) {
            i++
        }
        // If the current value from the right list is larger than the pivot
        // element then get the next element from the right list
        while (array[j] > pivot) {
            j--
        }

        // If we have found a value in the left list which is larger than
        // the pivot element and if we have found a value in the right list
        // which is smaller than the pivot element then we exchange the
        // values.
        // As we are done we can increase i and j
        if (i <= j) {
            exchange(i, j)
            i++
            j--
        }
    }
    // Recursion
    if (length < 10) {
        val def = mutableListOf<Deferred<Unit>>()
        if (low < j)
            def + async { quickSort(array, low, j) }
        if (i < high)
            def + async { quickSort(array, i, high) }

        def.forEach { it.await() }
    } else {
        if (low < j)
            quickSort(array, low, j)
        if (i < high)
            quickSort(array, i, high)
    }
}

private fun exchange(i: Int, j: Int) {
    val temp = numbers[i]
    numbers[i] = numbers[j]
    numbers[j] = temp
}
