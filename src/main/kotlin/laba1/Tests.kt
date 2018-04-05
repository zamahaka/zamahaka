package laba1

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val jobs = List(20_000) {
        async {
            delay(500)
            println("Finishing $it")
            IntArray(it) { it }.toList() as Collection<Int>
        }
    }

    val time = measureTimeMillis {
        val flatten = jobs.map { it.await() }.flatten()
        println("flatten $flatten")
    }

    println("Test map time: $time")
}