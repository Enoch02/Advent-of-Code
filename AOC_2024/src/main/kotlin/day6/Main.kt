package day6

import java.io.File

const val GUARD_DIRECTION_UP = '^'
const val GUARD_DIRECTION_DOWN = 'v'
const val GUARD_DIRECTION_LEFT = '<'
const val GUARD_DIRECTION_RIGHT = '>'
const val OBSTACLE = '#'
const val POSITION_VISITED = 'X'

data class ObstacleAheadResult(
    val isAhead: Boolean,
    val guardDirection: Char
)

fun isGuardOutsideMappedArea(mapRows: List<String>): Boolean {
    return mapRows.first().contains(GUARD_DIRECTION_UP) ||
            mapRows.last().contains(GUARD_DIRECTION_DOWN) ||
            mapRows.any { it.startsWith(GUARD_DIRECTION_LEFT) } ||
            mapRows.any { it.endsWith(GUARD_DIRECTION_RIGHT) }
}

fun isObstacleAhead(mapRows: List<String>): ObstacleAheadResult {
    val combinedMap = mapRows.joinToString("\n")

    return when {
        combinedMap.contains(GUARD_DIRECTION_UP) -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_UP) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)

            if (indexOfGuardRow == 0) {
                return ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_UP
                )
            }

            // Get the row above
            val rowAbove = mapRows[indexOfGuardRow - 1]
            val guardIndex = guardRow.indexOf(GUARD_DIRECTION_UP)

            if (guardIndex in rowAbove.indices && rowAbove[guardIndex] == OBSTACLE) {
                ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_UP
                )
            } else {
                ObstacleAheadResult(isAhead = false, guardDirection = GUARD_DIRECTION_UP)
            }
        }

        combinedMap.contains(GUARD_DIRECTION_DOWN) -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_DOWN) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)

            if (indexOfGuardRow == mapRows.lastIndex) {
                return ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_DOWN
                )
            }

            // Get the row below
            val rowBelow = mapRows[indexOfGuardRow + 1]
            val guardIndex = guardRow.indexOf(GUARD_DIRECTION_DOWN)

            if (guardIndex in rowBelow.indices && rowBelow[guardIndex] == OBSTACLE) {
                ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_DOWN
                )
            } else {
                ObstacleAheadResult(isAhead = false, guardDirection = GUARD_DIRECTION_DOWN)
            }
        }

        combinedMap.contains(GUARD_DIRECTION_LEFT) -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_LEFT) }
            val guardIndex = guardRow.indexOf(GUARD_DIRECTION_LEFT)

            if (guardIndex == 0) {
                return ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_LEFT
                )
            }

            if (guardRow[guardIndex - 1] == OBSTACLE) {
                ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_LEFT
                )
            } else {
                ObstacleAheadResult(isAhead = false, guardDirection = GUARD_DIRECTION_LEFT)
            }
        }

        combinedMap.contains(GUARD_DIRECTION_RIGHT) -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_RIGHT) }
            val guardIndex = guardRow.indexOf(GUARD_DIRECTION_RIGHT)

            if (guardIndex == guardRow.lastIndex) {
                return ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_RIGHT
                )
            }

            if (guardRow[guardIndex + 1] == OBSTACLE) {
                ObstacleAheadResult(
                    isAhead = true,
                    guardDirection = GUARD_DIRECTION_RIGHT
                )
            } else {
                ObstacleAheadResult(isAhead = false, guardDirection = GUARD_DIRECTION_RIGHT)
            }
        }

        else -> {
            ObstacleAheadResult(
                isAhead = false,
                guardDirection = 'o' // Default or invalid guard direction
            )
        }
    }
}

fun moveGuard(mapRows: List<String>, guardDirection: Char): List<String> {
    val mutableMapRows = mapRows.toMutableList()

    return when (guardDirection) {
        GUARD_DIRECTION_UP -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_UP.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_UP)

            val rowAbove = mapRows[indexOfGuardRow - 1]
            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, POSITION_VISITED)
            mutableMapRows[indexOfGuardRow - 1] =
                rowAbove.replaceCharAt(indexOfGuard, GUARD_DIRECTION_UP)

            mutableMapRows.toList()
        }

        GUARD_DIRECTION_DOWN -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_DOWN.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_DOWN)

            val rowBelow = mapRows[indexOfGuardRow + 1]
            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, POSITION_VISITED)
            mutableMapRows[indexOfGuardRow + 1] =
                rowBelow.replaceCharAt(indexOfGuard, GUARD_DIRECTION_DOWN)

            mutableMapRows.toList()
        }

        GUARD_DIRECTION_LEFT -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_LEFT.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_LEFT)

            mutableMapRows[indexOfGuardRow] = guardRow
                .replaceCharAt(indexOfGuard, POSITION_VISITED)
                .replaceCharAt(indexOfGuard - 1, GUARD_DIRECTION_LEFT)

            mutableMapRows.toList()
        }

        GUARD_DIRECTION_RIGHT -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_RIGHT.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_RIGHT)

            mutableMapRows[indexOfGuardRow] = guardRow
                .replaceCharAt(indexOfGuard, POSITION_VISITED)
                .replaceCharAt(indexOfGuard + 1, GUARD_DIRECTION_RIGHT)

            mutableMapRows.toList()
        }

        else -> {
            return mapRows
        }
    }
}

fun rotateGuard(mapRows: List<String>, guardDirection: Char): List<String> {
    val mutableMapRows = mapRows.toMutableList()

    when (guardDirection) {
        GUARD_DIRECTION_UP -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_UP.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_UP)

            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, GUARD_DIRECTION_RIGHT)
        }

        GUARD_DIRECTION_DOWN -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_DOWN.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_DOWN)

            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, GUARD_DIRECTION_LEFT)
        }

        GUARD_DIRECTION_LEFT -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_LEFT.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_LEFT)

            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, GUARD_DIRECTION_UP)
        }

        GUARD_DIRECTION_RIGHT -> {
            val guardRow = mapRows.first { it.contains(GUARD_DIRECTION_RIGHT.toString()) }
            val indexOfGuardRow = mapRows.indexOf(guardRow)
            val indexOfGuard = guardRow.indexOf(GUARD_DIRECTION_RIGHT)

            mutableMapRows[indexOfGuardRow] = guardRow.replaceCharAt(indexOfGuard, GUARD_DIRECTION_DOWN)
        }
    }

    return mutableMapRows.toList()
}

fun calculateDistinctPositions(map: String): Int {
    var mapRows = map.split("\n").toMutableList()

    val initialGuardRow = mapRows.first { it.contains('^') || it.contains('v') || it.contains('<') || it.contains('>') }
    val rowIndex = mapRows.indexOf(initialGuardRow)
    val guardIndex = initialGuardRow.indexOfFirst { it in listOf('^', 'v', '<', '>') }
    val guardChar = initialGuardRow[guardIndex]
    mapRows[rowIndex] = initialGuardRow.replaceCharAt(guardIndex, 'X')
        .replaceCharAt(guardIndex, guardChar)

    while (!isGuardOutsideMappedArea(mapRows)) {
        val result = isObstacleAhead(mapRows)

        mapRows = if (result.isAhead) {
            rotateGuard(mapRows, result.guardDirection).toMutableList()
        } else {
            moveGuard(mapRows, result.guardDirection).toMutableList()
        }
    }

    return mapRows.joinToString("\n").count { it == 'X' || it in listOf('^', 'v', '<', '>', 'X') }
}

data class GuardState(
    val position: Pair<Int, Int>,
    val direction: Char
)

fun findLoopingPositions(map: String): Int {
    val mapRows = map.split("\n")
    var count = 0

    for (row in mapRows.indices) {
        for (col in mapRows[row].indices) {
            if (mapRows[row][col] == '.') {
                // Create a new map with obstruction at this position
                val newMap = mapRows.mapIndexed { r, line ->
                    if (r == row) {
                        line.replaceCharAt(col, '#')
                    } else {
                        line
                    }
                }

                if (doesCreateLoop(newMap.joinToString("\n"))) {
                    count++
                }
            }
        }
    }

    return count
}

fun doesCreateLoop(map: String): Boolean {
    var mapRows = map.split("\n").toMutableList()
    val visited = mutableSetOf<GuardState>()

    val initialGuardRow = mapRows.first { it.contains('^') || it.contains('v') || it.contains('<') || it.contains('>') }
    val rowIndex = mapRows.indexOf(initialGuardRow)
    val guardIndex = initialGuardRow.indexOfFirst { it in listOf('^', 'v', '<', '>') }
    val initialDirection = initialGuardRow[guardIndex]

    var currentState = GuardState(rowIndex to guardIndex, initialDirection)

    while (!isGuardOutsideMappedArea(mapRows)) {
        if (!visited.add(currentState)) {
            return true // Loop found
        }

        val result = isObstacleAhead(mapRows)

        mapRows = if (result.isAhead) {
            rotateGuard(mapRows, result.guardDirection).toMutableList()
        } else {
            moveGuard(mapRows, result.guardDirection).toMutableList()
        }

        // Update current state
        val newGuardRow = mapRows.first { it.contains('^') || it.contains('v') || it.contains('<') || it.contains('>') }
        val newRowIndex = mapRows.indexOf(newGuardRow)
        val newGuardIndex = newGuardRow.indexOfFirst { it in listOf('^', 'v', '<', '>') }
        val newDirection = newGuardRow[newGuardIndex]

        currentState = GuardState(newRowIndex to newGuardIndex, newDirection)
    }

    return false // Guard left the area without looping
}

fun main() {
    val input = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

    println(calculateDistinctPositions(input))
    println(findLoopingPositions(input))

    val inputFile = File("src/main/kotlin/day6/input.txt")
    println(calculateDistinctPositions(inputFile.readText()))
    // super slow!!
    println(findLoopingPositions(inputFile.readText()))
}

fun String.replaceCharAt(index: Int, replacement: Char): String {
    if (index < 0 || index >= this.length) {
        throw IndexOutOfBoundsException("Index $index is out of bounds for length ${this.length}")
    }
    return this.substring(0, index) + replacement + this.substring(index + 1)
}