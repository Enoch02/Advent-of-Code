package day3

import java.io.File

fun getValidInstructions(from: String): Sequence<String> {
    val regex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    return regex.findAll(from).map { it.value }
}

fun sumAll(instructions: Sequence<String>): Int {
    val regex = "\\d+".toRegex()
    var sum = 0

    instructions.forEach { instruction ->
        val matches = regex.findAll(instruction).map { it.value.toInt() }.toList()
        val x = matches.first()
        val y = matches.last()
        sum += x * y
    }

    return sum
}

fun getValidInstructionsWithConditionals(from: String): Sequence<String> {
    val regex = """(mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\))""".toRegex()
    return regex.findAll(from).map { it.value }
}

fun sumAllWithConditionals(instructions: Sequence<String>): Int {
    val regex = "\\d+".toRegex()
    var mulEnabled = true
    var sum = 0

    instructions.forEach { instruction ->
        when {
            instruction.contains("mul") && mulEnabled -> {
                val matches = regex.findAll(instruction).map { it.value.toInt() }.toList()
                val x = matches.first()
                val y = matches.last()
                sum += x * y
            }

            instruction.contains("do()") -> {
                mulEnabled = true
            }

            instruction.contains("don't()") -> {
                mulEnabled = false
            }
        }
    }

    return sum
}

fun main() {
    val data = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    println(sumAll(getValidInstructions(data)))

    val filePath = "src/main/kotlin/day3/input.txt"
    val inputFile = File(filePath)
    println(sumAll(getValidInstructions(inputFile.readText())))
    println()

    val data1 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
    println(sumAllWithConditionals(getValidInstructionsWithConditionals(data1)))
    println(sumAllWithConditionals(getValidInstructionsWithConditionals(inputFile.readText())))
}