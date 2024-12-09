import java.io.File

private fun String.containsXmas(): Boolean {
    val query = "XMAS"
    return this.contains(query) || this.contains(query.reversed())
}

fun countXmas(input: String) {
    val lines = input.split("\n")
    var count = 0

    // Get actual dimensions of the input
    val rows = lines.size
    val cols = lines.firstOrNull()?.length ?: 0

    // Validate input
    if (rows == 0 || cols == 0) {
        println("Invalid input: empty grid")
        return
    }

    // Verify all rows have the same length
    if (lines.any { it.length != cols }) {
        println("Invalid input: inconsistent row lengths")
        return
    }

    // Check all possible directions
    for (row in 0 until rows) {
        for (col in 0 until cols) {
            // Check horizontal (right)
            if (col + 3 < cols) {
                val horizontal = StringBuilder()
                for (i in 0..3) {
                    horizontal.append(lines[row][col + i])
                }
                if (horizontal.toString().containsXmas()) count++
            }

            // Check vertical (down)
            if (row + 3 < rows) {
                val vertical = StringBuilder()
                for (i in 0..3) {
                    vertical.append(lines[row + i][col])
                }
                if (vertical.toString().containsXmas()) count++
            }

            // Check diagonal (down-right)
            if (row + 3 < rows && col + 3 < cols) {
                val diagonal = StringBuilder()
                for (i in 0..3) {
                    diagonal.append(lines[row + i][col + i])
                }
                if (diagonal.toString().containsXmas()) count++
            }

            // Check diagonal (down-left)
            if (row + 3 < rows && col - 3 >= 0) {
                val diagonal = StringBuilder()
                for (i in 0..3) {
                    diagonal.append(lines[row + i][col - i])
                }
                if (diagonal.toString().containsXmas()) count++
            }
        }
    }

    println("Found $count instances of XMAS")
}

fun main() {
    val input = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()

    countXmas(input)
    val inputFile = File("src/main/kotlin/day4/input.txt")
    countXmas(inputFile.readText())
}