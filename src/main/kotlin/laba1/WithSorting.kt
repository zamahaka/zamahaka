package laba1

import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

var numbers = randoms.toIntArray()

fun main(args: Array<String>) = runBlocking {
    val array = numbers.copyOf()
    println("Before sorting: ${array.toList().take(100)}")
    val time = measureTimeMillis { sort(array) }
    println("After sorting: ${array.toList().take(100)}")
    println("Iterative sorting time: $time")
}

fun sort(array: IntArray) = quickSort(array, 0, array.size - 1)


fun partition(arr: IntArray, left: Int, right: Int): Int {
    var i = left
    var j = right

    var tmp: Int

    val pivot = arr[(left + right) / 2]

    while (i <= j) {

        while (arr[i] < pivot) i++

        while (arr[j] > pivot) j--

        if (i <= j) {

            tmp = arr[i]

            arr[i] = arr[j]

            arr[j] = tmp

            i++

            j--

        }

    }

    return i
}


fun quickSort(arr: IntArray, left: Int, right: Int) {

    val index = partition(arr, left, right)

    if (left < index - 1)
        quickSort(arr, left, index - 1)

    if (index < right)
        quickSort(arr, index, right)

}