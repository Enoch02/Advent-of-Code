/**
 * Note to self: for counting problems like this, use maps to keep track of the counts
 * of elements instead of creating a massive list to hold all possible values.
 * That approach slows down to a crawl and consumes a lot of memory!
 */
package day11

import java.io.File
import kotlin.system.measureTimeMillis

class Solution {
    fun calculateStoneCountFor(blinks: Int, stones: String): Long {
        // Parse initial stones and store counts instead of values
        val stonesMap = mutableMapOf<Long, Long>()
        stones.trim().split(" ").forEach { stone ->
            val value = stone.toLong()
            stonesMap[value] = stonesMap.getOrDefault(value, 0L) + 1L
        }

        var stoneCount = stonesMap.values.sum()

        repeat(blinks) {
            val newStonesMap = mutableMapOf<Long, Long>()

            for ((stone, count) in stonesMap) {
                when {
                    stone == 0L -> {
                          // 0 becomes 1
                        newStonesMap[1L] = newStonesMap.getOrDefault(1L, 0L) + count
                    }

                    hasEvenDigits(stone) -> {
                        // Split number and add both parts to new map
                        val (left, right) = splitNumber(stone)
                        newStonesMap[left] = newStonesMap.getOrDefault(left, 0L) + count
                        newStonesMap[right] = newStonesMap.getOrDefault(right, 0L) + count

                        stoneCount += count
                    }

                    else -> {
                        // Odd digits: multiply by 2024
                        val newValue = stone * 2024
                        newStonesMap[newValue] = newStonesMap.getOrDefault(newValue, 0L) + count
                    }
                }
            }

            stonesMap.clear()
            stonesMap.putAll(newStonesMap)
        }

        return stoneCount
    }

    private fun hasEvenDigits(num: Long): Boolean {
        return num.toString().length % 2 == 0
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

    println("Example: ${solution.calculateStoneCountFor(25, "125 17")}")

    var timeTaken = measureTimeMillis {
        println("Part 1 Solution: ${solution.calculateStoneCountFor(25, inputFile.readText().trim())}\n")
    }
    println("Execution took $timeTaken ms\n")

    timeTaken = measureTimeMillis {
        println("Part 2 Solution: ${solution.calculateStoneCountFor(75, inputFile.readText().trim())}")
    }
    println("Execution took $timeTaken ms")
}