package days.aoc2024

import days.Day

class Day9 : Day(2024, 9) {
    override fun partOne(): Any {
        return calculatePartOne(inputString)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputString)
    }

    fun calculatePartOne(input: String): Long {
        val storage = IntArray(input.sumOf { it - '0' })
        var writeHead = 0
        var readTail = input.lastIndex
        var readHead = 0
        var endingFileSizeLeft = input[readTail] - '0'
        while (readHead < readTail) {
            for (file in 0 until input[readHead] - '0') {
                storage[writeHead++] = readHead / 2
            }
            readHead++

            for (empty in 0 until input[readHead] - '0') {
                while (endingFileSizeLeft <= 0) {
                    readTail -= 2
                    endingFileSizeLeft = input[readTail] - '0'
                }
                storage[writeHead++] = readTail / 2
                endingFileSizeLeft--
            }
            readHead++
        }

        while (endingFileSizeLeft > 0) {
            storage[writeHead++] = readTail / 2
            endingFileSizeLeft--
        }
        var sum = 0L
        for (i in 0..storage.lastIndex) {
            sum += storage[i] * i
        }

        return sum
    }

    fun calculatePartTwo(input: String): Long {
        var writeHead = 0
        var readHead = 0
        val freeSpaces = mutableListOf<FreeSpace>()
        val files = mutableListOf<File>()
        while (readHead <= input.lastIndex) {
            File(size = input[readHead] - '0', id = readHead / 2, location = writeHead).also {
                files.add(it)
                writeHead += it.size
            }
            readHead++

            if (readHead <= input.lastIndex) {
                FreeSpace(size = input[readHead] - '0', location = writeHead).also {
                    freeSpaces.add(it)
                    writeHead += it.size
                }
                readHead++
            }
        }
        for (index in files.lastIndex downTo 0) {
            try {
                val file = files[index]
                freeSpaces.first { it.size >= file.size && it.location < file.location }.let { freeSpace ->
                    files[index] = file.copy(location = freeSpace.location)
                    if (freeSpace.size == file.size) {
                        freeSpaces.remove(freeSpace)
                    } else {
                        freeSpaces[freeSpaces.indexOf(freeSpace)] = FreeSpace(
                            freeSpace.size - file.size,
                            freeSpace.location + file.size
                        )
                    }
                }
            } catch (e: NoSuchElementException) {
                // empty
            }
        }

        return files.sumOf { it.checksum() }
    }

    data class File(val size: Int, val id: Int, val location: Int) {
        fun checksum(): Long {
            var sum = 0L
            for (i in location until location + size) {
                sum += i * id
            }
            return sum
        }
    }

    data class FreeSpace(val size: Int, val location: Int)
}