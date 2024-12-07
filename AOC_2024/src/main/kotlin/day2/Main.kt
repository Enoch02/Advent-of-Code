package day2

import java.io.File
import kotlin.math.abs

fun checkLevelChange(arr: List<Int>): Boolean {
    if (arr.size <= 1) return true

    var isIncreasing = true
    var isDecreasing = true

    // skip the first element
    for (i in 1 until arr.size) {
        // Check if increasing property is changed
        if (arr[i] <= arr[i - 1]) {
            isIncreasing = false
        }

        // Check if decreasing property is changed
        if (arr[i] >= arr[i - 1]) {
            isDecreasing = false
        }

        if (!isIncreasing && !isDecreasing) {
            return false
        }
    }

    return isIncreasing || isDecreasing
}

fun checkAdjacentDifference(arr: List<Int>): Boolean {
    if (arr.size <= 1) return true

    for (i in 1 until arr.size) {
        val difference = abs(arr[i] - arr[i - 1])

        if (difference < 1 || difference > 3) {
            return false
        }
    }

    return true
}

fun checkReports(reports: List<List<Int>>): Int {
    var safeCount = 0

    reports.forEach { level ->
        if (checkLevelChange(level) && checkAdjacentDifference(level)) {
            safeCount += 1
        }
    }

    return safeCount
}

fun findElementToRemove(arr: List<Int>): Int {
    if (arr.size <= 1) return -1

    // Try removing each element and check if conditions are satisfied
    for (i in arr.indices) {
        // Create new array without element at index i
        val newArr = IntArray(arr.size - 1)
        var newIndex = 0
        for (j in arr.indices) {
            if (j != i) {
                newArr[newIndex++] = arr[j]
            }
        }

        if (checkLevelChange(newArr.toList()) && checkAdjacentDifference(newArr.toList())) {
            return i  // index of element to remove
        }
    }

    return -1  // No solution found
}

fun checkReportsWithDampener(reports: List<List<Int>>): Int {
    var safeCount = 0

    reports.forEach { level ->
        val indexToRemove = findElementToRemove(level)
        if (indexToRemove != -1) {
            safeCount++
        }
    }

    return safeCount
}

fun main() {
    val data = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent()
    val convertedData = data.split("\n")
        .map { levels ->
            levels.split(" ").map { it.toInt() }
        }
    val safeReportsCount = checkReports(convertedData)
    val safeReportsCountDampened = checkReportsWithDampener(convertedData)
    println("$safeReportsCount reports are safe")
    println("$safeReportsCountDampened reports are safe")

    val filePath = "src/main/kotlin/day2/input.txt"
    val inputFile = File(filePath)
    val inputData = mutableListOf<List<Int>>()
    inputFile.readLines()
        .forEach { level ->
            inputData.add(level.split(" ").map { it.toInt() })
        }

    println(checkReports(inputData))
    println(checkReportsWithDampener(inputData))
}