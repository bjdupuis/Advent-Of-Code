package days.aoc2015

import days.Day

class Day18: Day(2015, 18) {
    override fun partOne(): Any {
        return performLife(inputList, 100).map { string ->
            string.count { it == '#' }
        }.sum()
    }

    fun performLife(inputList: List<String>, steps: Int): List<String> {
        var result = mutableListOf<String>()
        result.addAll(inputList)
        for (i in 0 until steps) {
            result = calculateNextLifeState(result)
        }

        return result
    }

    private fun calculateNextLifeState(currentState: List<String>): MutableList<String> {
        val result = mutableListOf<String>()
        for (y in currentState.indices) {
            val line = currentState[y]
            val newLine = StringBuilder()
            for (x in line.indices) {
                val neighboringLightsOn = currentState.lifeNeighbors(x, y).count { it == '#' }
                newLine.append(when(line[x]) {
                    '#' -> {
                        if (neighboringLightsOn == 2 || neighboringLightsOn == 3) {
                            '#'
                        } else {
                            '.'
                        }
                    }
                    '.' -> {
                        if (neighboringLightsOn == 3) {
                            '#'
                        } else {
                            '.'
                        }
                    }
                    else -> error("oops")
                })
            }
            result.add(newLine.toString())
        }

        return result
    }

    override fun partTwo(): Any {
        var result = mutableListOf<String>()
        result.addAll(inputList)
        for (i in 0 until 100) {
            result[0] = '#' + result[0].substring(1, result[0].lastIndex) + '#'
            result[result.lastIndex] = '#' + result[result.lastIndex].substring(1, result[result.lastIndex].lastIndex) + '#'
            result = calculateNextLifeState(result)
        }

        result[0] = '#' + result[0].substring(1, result[0].lastIndex) + '#'
        result[result.lastIndex] = '#' + result[result.lastIndex].substring(1, result[result.lastIndex].lastIndex) + '#'
        return result.map { it.count { it == '#' } }.sum()
    }

}

fun List<String>.lifeNeighbors(charIndex: Int, lineIndex: Int) = sequence<Char> {
    for (y in (lineIndex - 1)..(lineIndex + 1))
        for (x in (charIndex - 1)..(charIndex + 1))
            if (!(y == lineIndex && x == charIndex) && y in indices && x in get(y).indices)
                yield(get(y)[x])

}