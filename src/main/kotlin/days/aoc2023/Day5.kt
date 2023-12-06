package days.aoc2023

import days.Day

class Day5 : Day(2023, 5) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    fun calculatePartOne(inputList: List<String>): Long {
        val seeds = inputList.first().split("\\s+".toRegex()).drop(1).map { it.toLong() }

        val maps = mutableListOf<SourceToDestinationMap>()

        var currentMap = SourceToDestinationMap()
        inputList.drop(2).forEach {line ->
            if (line.isEmpty()) {
                maps.add(currentMap)
                currentMap = SourceToDestinationMap()
            } else if (line.first().isDigit()) {
                parseRangeAndAddToMap(line, currentMap)
            }
        }

        return seeds.minOf { seed ->
            maps.fold(seed) { current, map ->
                map.destinationForSource(current)
            }
        }
    }

    private fun parseRangeAndAddToMap(line: String, map: SourceToDestinationMap) {
        Regex("(\\d+) (\\d+) (\\d+)").matchEntire(line)?.destructured?.let { (destinationStart, sourceStart, length) ->
            map.addRange(LongRange(sourceStart.toLong(), sourceStart.toLong() + length.toLong()), destinationStart.toLong() - sourceStart.toLong())
        }
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(inputList: List<String>): Long {
        val seedGroups = inputList.first().split("\\s+".toRegex()).drop(1).map { it.toLong() }.chunked(2)

        val maps = mutableListOf<SourceToDestinationMap>()

        var currentMap = SourceToDestinationMap()
        inputList.drop(2).forEach {line ->
            if (line.isEmpty()) {
                maps.add(currentMap)
                currentMap = SourceToDestinationMap()
            } else if (line.first().isDigit()) {
                parseRangeAndAddToMap(line, currentMap)
            }
        }

        var seedCount = 0L
        val result = seedGroups.minOf { seedGroup ->
            (seedGroup.first()..seedGroup.first() + seedGroup.last()).minOf {seed ->
                seedCount++
                maps.fold(seed) { current, map ->
                    map.destinationForSource(current)
                }
            }
        }
        println("Looked at $seedCount seeds")
        return result
    }

    private class SourceToDestinationMap {
        private val ranges = mutableListOf<Pair<LongRange,Long>>()

        fun addRange(range: LongRange, destinationOffset: Long) {
            ranges.add(Pair(range, destinationOffset))
        }

        fun destinationForSource(source: Long): Long {
            ranges.forEach { range ->
                if (range.first.contains(source)) {
                    return source + range.second
                }
            }

            return source
        }
    }
}