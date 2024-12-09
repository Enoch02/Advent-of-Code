package day7

import java.io.File

fun isValidExpression(testValue: Long, numbers: List<Long>): Boolean {
    val operators = listOf("+", "*", "||")

    // Generate all possible combinations of operators
    fun checkExpressions(currentExpression: List<String>, index: Int): Boolean {
        if (index == numbers.size - 1) {
            val expressionString = currentExpression.joinToString(" ")
            return evaluateLeftToRight(expressionString) == testValue
        }

        for (operator in operators) {
            val newExpression = currentExpression + listOf(operator.toString(), numbers[index + 1].toString())
            if (checkExpressions(newExpression, index + 1)) {
                return true // Early exit if a valid expression is found
            }
        }
        return false
    }

    return checkExpressions(listOf(numbers.first().toString()), 0)
}

fun evaluateLeftToRight(expression: String): Long {
    val tokens = expression.split(" ")
    var result = tokens[0].toLong()
    var i = 1
    while (i < tokens.size) {
        val operator = tokens[i]
        val nextValue = tokens[i + 1].toLong()
        result = when (operator) {
            "+" -> result + nextValue
            "*" -> result * nextValue
            "||" -> (result.toString() + nextValue.toString()).toLong()
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
        i += 2
    }
    return result
}

fun main() {
    val data = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()
    var result: Long = 0

    data.split("\n").forEach { input ->
        val parts = input.split(": ")
        val testValue = parts[0].toLong()
        val numbers = parts[1].split(" ").map { it.toLong() }

        if (isValidExpression(testValue, numbers)) {
            result += testValue
        }
    }
    println(result)

    result = 0
    val inputFile = File("src/main/kotlin/day7/input.txt")
    inputFile.readText().split("\n")
        .forEach { input ->
            val parts = input.split(": ")
            val testValue = parts[0].toLong()
            val numbers = parts[1].split(" ").map { it.toLong() }

            if (isValidExpression(testValue, numbers)) {
                result += testValue
            }
        }

    println(result)
}