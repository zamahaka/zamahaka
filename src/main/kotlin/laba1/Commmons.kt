package laba1

import java.util.*

fun min(first: Int?, second: Int?): Int? = when {
    first == null -> second
    second == null -> first
    else -> kotlin.math.min(first, second)
}

@Suppress("FunctionName")
fun RandomList(size: Int): List<Int> {
    val rng = Random()

    return IntArray(size) { rng.nextInt(size) }.toList()
}

val randoms by lazy { RandomList(3_000_000) }