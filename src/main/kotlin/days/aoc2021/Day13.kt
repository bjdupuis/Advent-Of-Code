package days.aoc2021

import days.Day

class Day13 : Day(2021, 13) {
    override fun partOne(): Any {
        return calculateDotsAfterFirstFold(inputList)
    }

    override fun partTwo(): Any {
        return calculateDotsAfterFolds(inputList)
    }

    fun calculateDotsAfterFirstFold(inputList: List<String>): Int {
        val extents = inputList.takeWhile { it.isNotBlank() }.map { line ->
            Regex("(\\d+),(\\d+)").matchEntire(line.trim())?.destructured?.let { (x, y) ->
                Pair(x.toInt(), y.toInt())
            }
        }.reduce { acc, pair ->
            acc?.let { a ->
                pair?.let { b->
                    Pair(maxOf(a.first, b.first), maxOf(a.second,b.second))
                }
            }
        } ?: throw IllegalStateException("Extents didn't work out")

        val paper = Array(extents.second + 1) {
            Array(extents.first + 1) { '.' }
        }

        inputList.takeWhile { it.isNotBlank() }.forEach { line ->
            Regex("(\\d+),(\\d+)").matchEntire(line.trim())?.destructured?.let { (x, y) ->
                paper[y.toInt()][x.toInt()] = '#'
            }
        }

        val folds = inputList.filter { it.startsWith("fold along") }

        Regex("fold along ([xy])=(\\d+)").matchEntire(folds[0].trim())?.destructured?.let { (axis, position) ->
            var folded = foldAlongAxis(paper, axis, position.toInt())

            return folded.sumBy { row -> row.filter { it == '#' }.count() }
        }

        return 0
    }

    fun calculateDotsAfterFolds(inputList: List<String>): Array<Array<Char>> {
        val extents = inputList.takeWhile { it.isNotBlank() }.map { line ->
            Regex("(\\d+),(\\d+)").matchEntire(line.trim())?.destructured?.let { (x, y) ->
                Pair(x.toInt(), y.toInt())
            }
        }.reduce { acc, pair ->
            acc?.let { a ->
                pair?.let { b ->
                    Pair(maxOf(a.first, b.first), maxOf(a.second, b.second))
                }
            }
        } ?: throw IllegalStateException("Extents didn't work out")

        val paper = Array(extents.second + 1) {
            Array(extents.first + 1) { '.' }
        }

        inputList.takeWhile { it.isNotBlank() }.forEach { line ->
            Regex("(\\d+),(\\d+)").matchEntire(line.trim())?.destructured?.let { (x, y) ->
                paper[y.toInt()][x.toInt()] = '#'
            }
        }

        var folded = paper
        inputList.filter { it.startsWith("fold along") }.forEach { fold ->
            Regex("fold along ([xy])=(\\d+)").matchEntire(fold.trim())?.destructured?.let { (axis, position) ->
                println("Folding along $axis axis at $position")
                folded = foldAlongAxis(folded, axis, position.toInt())
                dumpPaper(folded)
            }
        }

        return folded
    }

    private fun dumpPaper(paper: Array<Array<Char>>) {
        paper.forEach { row ->
            println(row.joinToString(""))
        }
    }

    private fun foldAlongAxis(paper: Array<Array<Char>> , axis: String, position: Int): Array<Array<Char>> {
        val folded = Array(if (axis == "y") paper.size / 2 else paper.size) {
            Array(if (axis == "x") paper.first().size / 2 else paper.first().size) { '.' }
        }

        when (axis) {
            "x" -> {
                val rightmost = paper.first().size - 1
                for (y in folded.indices) {
                    for (x in folded.first().indices) {
                        if (paper[y][x] == '#' || paper[y][rightmost - x] == '#')
                            folded[y][x] = '#'
                    }
                }
            }
            "y" -> {
                val bottom = paper.size - 1
                for (y in folded.indices) {
                    for (x in folded.first().indices) {
                        if (paper[y][x] == '#' || paper[bottom - y][x] == '#')
                            folded[y][x] = '#'
                    }
                }
            }
        }

        return folded
    }

}