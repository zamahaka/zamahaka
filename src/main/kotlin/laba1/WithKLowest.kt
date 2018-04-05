package laba1

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val findIn = ArrayList(randoms)

    val k = 5

    println("Result should be ${findIn.distinct().sorted().take(5).getOrNull(k - 1)}")
    println()

    var iterativeResult: Int? = null
    val iterativeTime = measureTimeMillis { iterativeResult = findIterative(k, findIn) }

    println()
    println("Iterative result: $iterativeResult with time: ${iterativeTime}ms")

    var parallelResult: Int? = null
    val parallelTime = measureTimeMillis { parallelResult = findParallel(k, findIn) }

    println()
    println("Parallel result: $parallelResult with time: ${parallelTime}ms")
}

fun findIterative(kValues: Int, `in`: List<Int>) =
        findMin(kValues, `in`).toList().sorted().getOrNull(kValues - 1)

suspend fun findParallel(kValues: Int, `in`: List<Int>): Int? {
    val size = `in`.size / (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)

    val chunks = `in`.chunked(size)

    val jobs = chunks.map { async { findMin(kValues, it) } }

    val results = jobs.map { it.await() }.flatten()

    return findMin(kValues, results).toList().distinct().sorted().getOrNull(kValues - 1)
}

fun findMin(kValues: Int, `in`: List<Int>): Collection<Int> {
    val findIn = `in`

    val resultSet = findIn.toMutableSet().take(kValues).toMutableSet()

    fun MutableCollection<Int>.changeMaxTo(value: Int) {
        remove(max())

        add(value)
    }

    findIn.forEach { currentInList ->
        resultSet.max()?.let { setMax ->
            if (currentInList < setMax && !resultSet.contains(currentInList)) {
                resultSet.changeMaxTo(currentInList)
            }
        }
    }

    return resultSet
}