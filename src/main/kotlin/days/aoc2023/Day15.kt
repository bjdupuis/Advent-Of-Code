package days.aoc2023

import days.Day

class Day15 : Day(2023, 15) {
    override fun partOne(): Any {
        return calculatePartOne(inputString)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputString)
    }

    fun calculatePartOne(input: String): Int {
        return input.split(",").sumOf { hashValueOf(it) }
    }

    private fun hashValueOf(string: String): Int {
        var hash = 0
        string.forEach { c ->
            hash = ((hash + c.code) * 17) % 256
        }
        return hash
    }

    fun calculatePartTwo(input: String): Int {
        val boxes = mutableMapOf<Int,MutableList<Pair<String,Int>>>()
        input.split(",").forEach { step ->
            Regex("(\\w+)([-=])(\\d*)").matchEntire(step)?.destructured?.let { (label,operation,focalLength) ->
                val boxNumber = hashValueOf(label)
                val lensList = boxes.getOrPut(boxNumber) { mutableListOf() }
                when (operation) {
                    "=" -> {
                        val newLens = Pair(label, focalLength.toInt())
                        if (lensList.any { lens -> lens.first == label } ) {
                            lensList[lensList.indexOfFirst { lens -> lens.first == label }] = newLens
                        } else {
                            lensList.add(newLens)
                        }
                    }
                    "-" -> {
                        lensList.removeIf { lens -> lens.first == label }
                    }
                    else -> throw IllegalArgumentException("that shouldn't have been in there")
                }
            }
        }

        return boxes.entries.sumOf { box ->
            box.value.withIndex().sumOf { (lens, lensIndex) ->
                (box.key + 1) * (lens + 1) * lensIndex.second
            }
        }
    }
}