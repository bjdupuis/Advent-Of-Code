package days.aoc2020

import days.Day

class Day16: Day(2020, 16) {
    override fun partOne(): Any {
        val ranges = parseRanges(inputList.takeWhile { it.isNotBlank() })
        val rawRanges = ranges.values

        return inputList.subList(inputList.indexOf("nearby tickets:")+1, inputList.size).map {
            it.split(",").map { it.toInt() }.filter { value ->
                rawRanges.none { it.contains(value) }
            }.sum()
        }.sum()
    }

    fun parseRanges(list: List<String>): Map<String,Set<Int>> {
        val result = mutableMapOf<String,Set<Int>>()

        list.forEach {
            Regex("(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)").matchEntire(it)?.destructured?.let {
                (name, rangeOneStart, rangeOneEnd, rangeTwoStart, rangeTwoEnd) ->
                result[name] = mutableSetOf<Int>().plus(rangeOneStart.toInt()..rangeOneEnd.toInt())
                        .plus(rangeTwoStart.toInt()..rangeTwoEnd.toInt())
            }
        }

        return result
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(list: List<String>): Long {
        val ranges = parseRanges(list.takeWhile { it.isNotBlank() })

        val validTickets = list.subList(list.indexOf("nearby tickets:")+1, list.size).filter {
            it.split(",").map { it.toInt() }.none { value ->
                ranges.values.none { it.contains(value) }
            }
        }

        val validRangeNamesForColumns = mutableMapOf<Int,MutableList<String>>()
        var done = false
        var index = 0
        val myTicket = list[list.indexOf("your ticket:")+1]!!
        var currentTicket = myTicket
        while (!done) {
            currentTicket.split(",").forEachIndexed { column, s ->
                val value = s.toInt()
                if (validRangeNamesForColumns[column] == null) {
                    validRangeNamesForColumns[column] = mutableListOf<String>().apply {
                        addAll(ranges.keys)
                    }
                }

                validRangeNamesForColumns[column] = validRangeNamesForColumns[column]?.minus(ranges.entries.filterNot { entry ->
                    entry.value.contains(value)
                }.map { it.key })?.toMutableList() ?: error("Oops")
            }

            do {
                // check to see if any name only appears once in all the columns
                var entitiesRemoved = false
                ranges.keys.filter { rangeName ->
                    validRangeNamesForColumns.filterValues { it.contains(rangeName) }.size == 1
                }.forEach { uniqueRangeName ->
                    validRangeNamesForColumns.filter {
                        it.value.contains(uniqueRangeName)
                    }.forEach {
                        entitiesRemoved = it.value.removeIf { it != uniqueRangeName }
                    }
                }

                // check for single valid name for column
                validRangeNamesForColumns.filter {
                    it.value.size == 1
                }.flatMap {
                    it.value
                }.forEach { toRemove ->
                    validRangeNamesForColumns.forEach { entry ->
                        if (entry.value.size > 1) {
                            entitiesRemoved = entitiesRemoved or entry.value.remove(toRemove)
                        }
                    }
                }
            } while(entitiesRemoved)

            if (validRangeNamesForColumns.filter { it.value.size > 1 }.isEmpty()) {
                done = true
            } else {
                currentTicket = validTickets[index]
                index++
            }
        }

        val myIntTicket = myTicket.split(",").map { it.toInt() }

        // now we have our column names, find our ticket "code"
        return validRangeNamesForColumns.filter {
            it.value[0]?.startsWith("departure")
        }.map { entry ->
            myIntTicket[entry.key]!!.toLong()
        }.reduce { acc, i -> acc * i }
    }
}