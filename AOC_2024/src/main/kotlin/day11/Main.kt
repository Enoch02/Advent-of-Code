package day11

import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * LRU Cache implementation in Kotlin
 * This class provides a way to cache function results based on their arguments
 * and automatically evicts the least recently used entries when the cache reaches its capacity.
 */
class LRUCache<K, V>(private val capacity: Int) {
    private val cache = LinkedHashMap<K, V>(capacity, 0.75f, true)

    fun get(key: K): V? {
        return cache[key]
    }

    fun put(key: K, value: V): V? {
        val previous = cache.put(key, value)
        if (cache.size > capacity) {
            val eldest = cache.entries.iterator().next()
            cache.remove(eldest.key)
        }
        return previous
    }

    fun containsKey(key: K): Boolean {
        return cache.containsKey(key)
    }

    fun size(): Int {
        return cache.size
    }

    fun clear() {
        cache.clear()
    }
}

/**
 * Function memoization with LRU caching using Kotlin's delegation pattern
 */
class LRUCacheFunction<P, R>(
    private val capacity: Int,
    private val function: (P) -> R
) : (P) -> R {
    private val cache = LRUCache<P, R>(capacity)

    override fun invoke(param: P): R {
        return cache.get(param) ?: function(param).also { cache.put(param, it) }
    }
}

/**
 * Extension function to make any function LRU cached
 */
fun <P, R> ((P) -> R).withLRUCache(capacity: Int): (P) -> R {
    return LRUCacheFunction(capacity, this)
}

/**
 * Extension function for functions with 2 parameters
 */
fun <P1, P2, R> ((P1, P2) -> R).withLRUCache(capacity: Int): (P1, P2) -> R {
    val cached = LRUCacheFunction<Pair<P1, P2>, R>(capacity) { (p1, p2) -> this(p1, p2) }
    return { p1, p2 -> cached(Pair(p1, p2)) }
}

/**
 * Extension function for functions with 3 parameters
 */
fun <P1, P2, P3, R> ((P1, P2, P3) -> R).withLRUCache(capacity: Int): (P1, P2, P3) -> R {
    // We use Triple to wrap three parameters
    val cached = LRUCacheFunction<Triple<P1, P2, P3>, R>(capacity) { (p1, p2, p3) -> this(p1, p2, p3) }
    return { p1, p2, p3 -> cached(Triple(p1, p2, p3)) }
}

class Solution {
    /*fun countStones(stones: String, targetBlinks: Int): Int {
        val stonesList = stones
            .split(" ")

        return stonesList.sumOf { improvedCalculateStoneCount(0, targetBlinks, it) }
    }

    private fun improvedCalculateStoneCount(blinkCount: Int, targetBlinks: Int, stone: String): Int {
        val digits = stone.length
        var nStones = 0

        if (blinkCount == targetBlinks) {
            return 1
        }

        when {
            stone == "0" -> {
                nStones += improvedCalculateStoneCount(blinkCount + 1, targetBlinks, "1")
            }

            digits % 2 == 0 -> {
                val (left, right) = splitNumber(stone.toLong())

                nStones += improvedCalculateStoneCount(blinkCount + 1, targetBlinks, left.toString())
                nStones += improvedCalculateStoneCount(blinkCount + 1, targetBlinks, right.toString())
            }

            else -> {
                val s = stone.toLong() * 2024
                nStones += improvedCalculateStoneCount(blinkCount + 1, targetBlinks, s.toString())
            }
        }

        return nStones
    }*/

    // TODO: reduce memory used by lists
    fun calculateStoneCountFor1(blinks: Int, stones: String): Int {
        var intStones = stones
            .split(" ")
            .map { it.toLong() }
            .toMutableList()

        repeat(blinks) {
            val temp = mutableListOf<Long>()

            intStones.forEach { stone ->
                when {
                    stone == 0.toLong() -> {
                        temp.add(1)
                    }

                    hasEvenDigits(stone) -> {
                        val (left, right) = splitNumber(stone)

                        temp.add(left)
                        temp.add(right)
                    }

                    else -> {
                        temp.add(stone * 2024)
                    }
                }
            }

            intStones.clear()
            intStones = temp
        }

        return intStones.size
    }

    /*fun calculateStoneCountFor(blinks: Int, stones: String): Int {
        val intStones = stones
            .split(" ")
            .map { it.toLong() }
        var count = 0

        for (stone in intStones) {
            println("Counting for $stone")
            count += countStones(blinks = blinks, targetBlinks = 0, stone = stone)
        }

        return count
    }

    private fun countStones(blinks: Int, targetBlinks: Int, stone: Long): Int {
        if (targetBlinks == blinks) {
            return 1
        }
        var count = 0

        //TODO: recursive case here
        when {
            stone == 0L -> {
                count += countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = 1)
            }

            hasEvenDigits(stone) -> {
                val (left, right) = splitNumber(stone)

                count += countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = left)
                count += countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = right)
            }

            else -> {
                count += countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = stone * 2024)
            }
        }

        return count
    }*/

    suspend fun calculateStoneCountFor(blinks: Int, stones: String): Long {
        val intStones = stones
            .split(" ")
            .map { it.toLong() }

        return coroutineScope {
            intStones.map { stone ->
                async(Dispatchers.Default) {
                    countStones(blinks = blinks, targetBlinks = 0, stone = stone)
                }
            }.awaitAll().sum()
        }
    }

    /*private fun countStones(blinks: Int, targetBlinks: Int, stone: Long): Int {
        if (targetBlinks == blinks) {
            return 1
        }

        return when {
            stone == 0L -> {
                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = 1)
            }

            hasEvenDigits(stone) -> {
                val (left, right) = splitNumber(stone)
                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = left) +
                        countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = right)
            }

            else -> {
                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = stone * 2024)
            }
        }
    }*/
    private fun countStones(blinks: Int, targetBlinks: Int, stone: Long): Long {
        if (targetBlinks == blinks) {
            return 1
        }

        val stoneCounter = { blinkz: Int, targetBlinkz: Int, stonz: Long ->
            countStones(blinkz, targetBlinkz, stonz)
        }.withLRUCache(1024)

        return when {
            stone == 0L -> {
//                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = 1)
                stoneCounter(blinks, targetBlinks + 1, 1)
            }

            hasEvenDigits(stone) -> {
                val (left, right) = splitNumber(stone)
//                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = left) + countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = right)
                stoneCounter(blinks, targetBlinks + 1, left) + stoneCounter(blinks, targetBlinks + 1, right)
            }

            else -> {
//                countStones(blinks = blinks, targetBlinks = targetBlinks + 1, stone = stone * 2024)
                stoneCounter(blinks, targetBlinks + 1, stone * 2024)
            }
        }
    }

    private fun hasEvenDigits(num: Long): Boolean {
        val digitCount = num.toString().length
        return digitCount % 2 == 0
    }

    private fun splitNumber(num: Long): Pair<Long, Long> {
        val numberStr = num.toString()
        val mid = numberStr.length / 2
        val left = numberStr.substring(0, mid).toLong()
        val right = numberStr.substring(mid).toLong()
        return Pair(left, right)
    }
}

fun main() {
    val inputFile = File("src/main/kotlin/day11/input.txt")
    val solution = Solution()

    runBlocking { println("Example: ${solution.calculateStoneCountFor(25, "125 17")}") }

    var timeTaken = measureTimeMillis {
        runBlocking { println("Part 1 Solution: ${solution.calculateStoneCountFor(25, inputFile.readText())}") }
    }
    println("Execution took $timeTaken ms\n")

    /*timeTaken = measureTimeMillis {
        println("Part 1 Solution (Improved Impl): ${solution.countStones(inputFile.readText(), 25)}")
    }
    println("Execution took $timeTaken ms\n")*/

    timeTaken = measureTimeMillis {
//        println("Part 2 Solution: ${solution.countStones(inputFile.readText(), 75)}")
        runBlocking { println("Part 2 Solution: ${solution.calculateStoneCountFor(75, inputFile.readText())}") }
    }
    println("Execution took $timeTaken ms")
}