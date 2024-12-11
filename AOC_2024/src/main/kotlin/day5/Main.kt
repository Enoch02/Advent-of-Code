package day5

import java.io.File

fun doesRuleHold(lhs: String, rhs: String, pagesSplit: List<String>): Boolean {
    val indexOfLhs = pagesSplit.indexOf(lhs)
    val indexOfRhs = pagesSplit.indexOf(rhs)
    return indexOfLhs < indexOfRhs
}

fun isUpdateCorrect(rules: List<String>, updates: String): Boolean {
    val pagesSplit = updates.split(',')

    for (rule in rules) {
        val ruleComponents = rule.split('|')
        if (pagesSplit.contains(ruleComponents[0]) && pagesSplit.contains(ruleComponents[1])) {
            if (!doesRuleHold(ruleComponents[0], ruleComponents[1], pagesSplit)) {
                return false
            }
        }
    }
    return true
}

fun findMiddlePageSum(input: List<String>) {
    val rules = input[0].split('\n')
    val updates = input[1].split('\n')
    var middleSum = 0

    for (update in updates) {
        if (isUpdateCorrect(rules, update)) {
            println("$update is correct")
            val pages = update.split(',')
            val middleIndex = pages.size / 2
            middleSum += pages[middleIndex].toInt()
        }
    }

    println("Sum of middle values: $middleSum")
}

fun sortByRules(pages: List<String>, rules: List<String>): List<String> {
    // Convert rules to map of page -> pages that must come after it
    val dependencies = mutableMapOf<String, MutableSet<String>>()
    for (rule in rules) {
        val (before, after) = rule.split('|')
        if (pages.contains(before) && pages.contains(after)) {
            dependencies.getOrPut(before) { mutableSetOf() }.add(after)
        }
    }

    // Topological sort
    val result = mutableListOf<String>()
    val visited = mutableSetOf<String>()
    val temp = mutableSetOf<String>()

    fun visit(page: String) {
        if (temp.contains(page)) return  // Handle cycles
        if (visited.contains(page)) return

        temp.add(page)
        dependencies[page]?.forEach { nextPage ->
            visit(nextPage)
        }
        temp.remove(page)
        visited.add(page)
        result.add(0, page)
    }

    pages.forEach { page ->
        if (!visited.contains(page)) {
            visit(page)
        }
    }

    return result.reversed()
}

fun getMiddleValue(pages: List<String>): Int {
    return if (pages.size % 2 == 0) {
        val middleIndex1 = pages.size / 2 - 1
        val middleIndex2 = pages.size / 2
        (pages[middleIndex1].toInt() + pages[middleIndex2].toInt()) / 2
    } else {
        val middleIndex = pages.size / 2
        pages[middleIndex].toInt()
    }
}

fun findMiddlePageSum1(input: List<String>) {
    val rules = input[0].split('\n')
    val updates = input[1].split('\n')
    var middleSum = 0

    for (update in updates) {
        if (!isUpdateCorrect(rules, update)) {
            val pages = update.split(',')
            val sortedPages = sortByRules(pages, rules)
            val middleValue = getMiddleValue(sortedPages)
            println("$update -> ${sortedPages.joinToString(",")}, middle: $middleValue")
            middleSum += middleValue
        }
    }
    println("Sum of middle values of incorrect updates: $middleSum")
}

fun main() {
    val data = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()
    findMiddlePageSum(data.split("\n\n"))
    println()

    val inputFile = File("src/main/kotlin/day5/input.txt")
    //findMiddlePageSum(inputFile.readText().split("\n\n"))

    findMiddlePageSum1(data.split("\n\n"))
    findMiddlePageSum1(inputFile.readText().split("\n\n"))
}