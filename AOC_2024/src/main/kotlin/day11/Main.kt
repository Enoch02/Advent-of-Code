package day11

import java.io.File
import kotlin.system.measureTimeMillis

class Solution {
    fun calculateStoneCountFor(blinks: Int, stones: String): Int {
        var intStones = stones
            .split(" ")
            .map { it.toLong() }
            .toMutableList()

        repeat(blinks) {
            val temp = mutableListOf<Long>()

            intStones.forEachIndexed { index, stone ->
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

            intStones = temp
        }

        return intStones.size
    }

    private fun hasEvenDigits(num: Long): Boolean {
        val digitCount = num.toString().length
        return digitCount % 2 == 0
    }

    private fun splitNumber(num: Long): Pair<Long, Long> {
        val digits = num.toString()
        val mid = digits.length / 2

        val left = digits.substring(0, mid).toLongOrNull() ?: 0
        val right = digits.substring(mid).toLongOrNull() ?: 0

        return Pair(left, right)
    }
}

fun main() {
    val solution = Solution()
    println(solution.calculateStoneCountFor(25, "125 17"))

    val inputFile = File("src/main/kotlin/day11/input.txt")
    val timeTaken = measureTimeMillis {
        println("Part 1 Solution: ${solution.calculateStoneCountFor(25, inputFile.readText())}")
    }
    println("Execution took $timeTaken ms")
}