package laba1

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val k = 5

    printIterative(k)

    for (chunkSize in 1_000..20_000 step 1_000) printParallel(k, chunkSize)

}

private fun printIterative(k: Int) {
    var iterativeResult: Int? = null
    val iterativeTime = measureTimeMillis {
        iterativeResult = randoms.findKValue(k) { min -> filter { it > min }.min() }
    }

    println("Iterative time: $iterativeTime result: $iterativeResult")
}

private suspend fun printParallel(k: Int, chunkSize: Int) {
    var parallelResult: Int? = null
    val parallelTime = measureTimeMillis {
        parallelResult = parallel(chunkSize, k)
    }

    println("Parallel time: $parallelTime result: $parallelResult chunkSize: $chunkSize")
}

private suspend fun parallel(chunkSize: Int, k: Int): Int? {
    var previous: Int? = Int.MIN_VALUE

    val size = randoms.size / chunkSize
    val chunks = randoms.chunked(size)

    for (i in 0 until k) {
        previous = previous?.let { previous ->
            val jobs = chunks.map { async { it.filter { it > previous }.min() } }

            jobs.mapNotNull { it.await() }.min()
        } ?: break
    }

    return previous
}

fun List<Int>.findKValue(k: Int, withFind: List<Int>.(min: Int) -> Int?): Int? {
    var previous: Int? = Int.MIN_VALUE

    for (i in 0 until k) previous = previous?.let { withFind(it) }

    return previous
}