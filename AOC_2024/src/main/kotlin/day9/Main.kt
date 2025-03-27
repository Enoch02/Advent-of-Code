package day9

import java.io.File
import kotlin.system.measureTimeMillis

class FileCompacter(diskMap: String) {
    private val diskMapList = diskMap.map { it.toString() }

    private val blocks = mutableListOf<String>()
    var checksum = 0L

    init {
        generateFileIds()
        rearrangeFileBlocks()
        calculateChecksum()
    }

    private fun generateFileIds() {
        var id = 0

        diskMapList.forEachIndexed { index: Int, data: String ->
            if (index % 2 == 0) {
                repeat(data.toInt()) {
                    blocks.add("$id")
                }

                id++
            } else {
                repeat(data.toInt()) {
                    blocks.add(".")
                }
            }
        }

//        println("Blocks: ${blocks.joinToString("")}")
    }

    private fun rearrangeFileBlocks() {
        var end = blocks.lastIndex

        while (end != 0) {
            val closestFreeSpaceIndex = blocks.indexOfFirst { it == "." }

            if (closestFreeSpaceIndex == -1) {
                break
            }

            if (end < closestFreeSpaceIndex) {
                break
            }

            if (blocks[end] != ".") {
                blocks[closestFreeSpaceIndex] = blocks[end]
                blocks[end] = "."
            }

            end--
        }

//        println("Arranged Blocks: ${blocks.joinToString("")}")
    }

    fun calculateChecksum(): Long {
        for ((index, data) in blocks.withIndex()) {
            if (data != ".") {
                checksum += index * data.toInt()
            }
        }

        return checksum
    }
}

fun main() {
    val sampleInputs = listOf("12345", "90909", "2333133121414131402")

    sampleInputs.forEach { input ->
        val compacter = FileCompacter(input)
        println("Checksum: ${compacter.calculateChecksum()}")
    }
    println()

    val timeTaken = measureTimeMillis {
        val inputFile = File("src/main/kotlin/day9/input.txt")
        val puzzleCompacter = FileCompacter(inputFile.readText())
        println("Puzzle Solution: ${puzzleCompacter.checksum}")
    }
    println("Execution took $timeTaken ms")
}