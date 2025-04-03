package day9

import java.io.File
import kotlin.system.measureTimeMillis

class FileCompacter(diskMap: String, part1: Boolean) {
    private val diskMapList = diskMap.map { it.toString() }

    private val blocks = mutableListOf<String>()
    var checksum = 0L

    init {
        generateFileIds()
        if (part1) {
            rearrangeFileBlocks()
        } else {
            rearrangeFiles()
        }
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
    }

    // part 2
    private fun rearrangeFiles() {
        var end = blocks.lastIndex
        val freeSpaceData = computeFreeSpaceData()

        while (end > 1) {
            val block = blocks[end]
            if (block == ".") {
                end--
                continue
            }

            val fileInfo = getFileInfo(end, block)
            val freeSpaceInfoIndex = freeSpaceData.indexOfFirst { it.size >= fileInfo.size }
            if (freeSpaceInfoIndex == -1) {
                end -= fileInfo.size
                continue
            }

            val freeSpaceInfo = freeSpaceData[freeSpaceInfoIndex]
            if (freeSpaceInfo.firstBlockIndex >= fileInfo.firstBlockIndex) {
                end -= fileInfo.size
                continue
            }

            moveFileToFreeSpace(freeSpaceInfo, fileInfo)

            // Update free space data
            freeSpaceData[freeSpaceInfoIndex] = freeSpaceInfo.copy(
                size = freeSpaceInfo.size - fileInfo.size,
                firstBlockIndex = freeSpaceInfo.firstBlockIndex + fileInfo.size
            )

            freeSpaceData.removeIf { it.size == 0 }
            end -= fileInfo.size
        }
    }

    private fun computeFreeSpaceData(): MutableList<FreeSpaceInfo> {
        val data = mutableListOf<FreeSpaceInfo>()
        var currentIndex = 0

        while (currentIndex < blocks.size) {
            if (blocks[currentIndex] != ".") {
                currentIndex++
                continue
            }

            val firstBlockIndex = currentIndex
            while (currentIndex < blocks.size && blocks[currentIndex] == ".") {
                currentIndex++
            }

            data.add(FreeSpaceInfo(size = currentIndex - firstBlockIndex, firstBlockIndex = firstBlockIndex, lastBlockIndex = currentIndex - 1))
        }

        return data
    }

    private fun getFileInfo(fromBlockIndex: Int, fileId: String): FileInfo {
        var currentIndex = fromBlockIndex
        var size = 0

        while (blocks[currentIndex] == fileId) {
            if (currentIndex == 0) {
                break
            }

            size++
            currentIndex--
        }

        return FileInfo(
            size = size,
            firstBlockIndex = currentIndex + 1,
            lastBlockIndex = fromBlockIndex
        )
    }

    private fun moveFileToFreeSpace(freeSpaceInfo: FreeSpaceInfo, fileInfo: FileInfo) {
        var freeSpaceIndex = freeSpaceInfo.firstBlockIndex
        var fileIndex = fileInfo.firstBlockIndex

        while (freeSpaceIndex <= freeSpaceInfo.lastBlockIndex) {
            if (fileIndex <= fileInfo.lastBlockIndex) {
                blocks[freeSpaceIndex] = blocks[fileIndex]
                blocks[fileIndex] = "."

                fileIndex++
            } else {
                break
            }

            freeSpaceIndex++
        }
    }

    private fun calculateChecksum() {
        for ((index, data) in blocks.withIndex()) {
            if (data != ".") {
                checksum += index * data.toInt()
            }
        }
    }

    data class FreeSpaceInfo(
        val size: Int,
        val firstBlockIndex: Int,
        val lastBlockIndex: Int
    )

    data class FileInfo(
        val size: Int,
        val firstBlockIndex: Int,
        val lastBlockIndex: Int
    )
}

fun main() {
    val sampleInputs = listOf("12345", "90909", "2333133121414131402")

    println("What part? (1/2)")
    val part = readlnOrNull()

    sampleInputs.forEach { input ->
        val compacter = FileCompacter(input, part == "1")
        println("Input $input -> Checksum: ${compacter.checksum}")
    }
    println()

    val timeTaken = measureTimeMillis {
        val inputFile = File("src/main/kotlin/day9/input.txt")
        val puzzleCompacter = FileCompacter(inputFile.readText(), part == "1")

        if (part == "1") {
            println("Part 1 Solution: ${puzzleCompacter.checksum}")
        } else {
            println("Part 2 Solution: ${puzzleCompacter.checksum}")
        }
    }
    println("Execution took $timeTaken ms")
}