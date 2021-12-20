package days.aoc2021

import days.Day

class Day20 : Day(2021, 20) {
    override fun partOne(): Any {
        val pair = parseInput(inputList)
        val image = pair.second.enhance(pair.first, 2)
        return countLightPixels(image)
    }

    override fun partTwo(): Any {
        val pair = parseInput(inputList)
        val image = pair.second.enhance(pair.first, 50)
        return countLightPixels(image)
    }

    fun parseInput(inputLines: List<String>): Pair<String,Image> {
        val algorithm = inputLines.first()
        val imageData = inputLines.drop(2)

        // make it larger than it needs to be to account for the empty ring around it
        val data = Array(imageData.first().length) {
            Array(imageData.size) {'.'}
        }

        imageData.forEachIndexed { y, line ->
            line.forEachIndexed { x, c -> data[y][x] = c }
        }

        return Pair(algorithm, Image(data))
    }

    fun countLightPixels(image: Image): Int {
        return image.data.sumOf { row -> row.count { it == '#' } }
    }

    class Image(val data: Array<Array<Char>>) {
        fun enhance(algorithm: String, times: Int): Image {
            var current = data
            repeat (times) { step ->
                val newData = Array(current.size + 4) {
                    Array(current.first().size + 4) { if (algorithm[0] == '#' && (step % 2 == 1)) '#' else '.' }
                }

                for (y in newData.indices) {
                    for (x in newData.first().indices) {
                        var binaryString = ""
                        neighbors(y, x).forEach {
                            val row = current.getOrElse(it.first - 2) { arrayOf() }
                            val value = if (row.indices.contains(it.second - 2)) {
                                row[it.second - 2]
                            } else {
                                if (algorithm[0] == '#' && (step % 2 == 1)) '#' else '.'
                            }
                            binaryString += if (value == '#') '1' else '0'
                        }
                        newData[y][x] = algorithm[convertBinaryToInt(binaryString)]
                    }
                }
                current = newData
            }

            return Image(current)
        }

        private fun neighbors(targetY: Int, targetX: Int) = sequence {
            for (y in targetY - 1..targetY + 1)
                for (x in targetX - 1..targetX + 1)
                    yield(Pair(y,x))
        }

        private fun convertBinaryToInt(binary: String?): Int {
            return binary?.reversed()?.foldIndexed(0) { index, total, digit ->
                total + 1.shl(index) * (digit - '0')
            } ?: throw IllegalArgumentException()
        }

        private fun dump() {
            for (y in data.indices) {
                for (x in data[y].indices) {
                    print(data[y][x])
                }
                println()
            }
            println()
        }

    }


}