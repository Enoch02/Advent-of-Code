package day1

import java.io.File
import kotlin.math.abs

fun calculateDistance(list1: List<Int>, list2: List<Int>): Int {
    val list1Sorted = list1.sorted()
    val list2Sorted = list2.sorted()
    val distances = mutableMapOf<Int, Int>()

    for (i in list1Sorted.indices) {
        distances[i] = abs(list1Sorted[i] - list2Sorted[i])
    }

    return distances.values.fold(0) { acc, i -> acc + i }
}

fun calculateSimilarityScore(list1: List<Int>, list2: List<Int>): Int {
    val frequencyMap = list2.groupingBy { it }.eachCount()
    var totalScore = 0

    list1.forEach { number ->
        val frequency = frequencyMap[number] ?: 0
        val score = number * frequency
        totalScore += score
    }

    return totalScore
}

fun main() {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    val filePath = "src/main/kotlin/day1/input.txt"

    File(filePath).forEachLine { line ->
        val parts = line.split("\\s+".toRegex()) // Split by whitespace
        if (parts.size == 2) {
            list1.add(parts[0].toInt())
            list2.add(parts[1].toInt())
        }
    }

    val distance = calculateDistance(
        list1 = list1,
        list2 = list2
    )
    println("Distance: $distance")

    val similarity = calculateSimilarityScore(
        list1 = list1,
        list2 = list2
    )
    println("Similarity: $similarity")
}