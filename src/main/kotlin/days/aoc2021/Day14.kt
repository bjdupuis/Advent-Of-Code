package days.aoc2021

import days.Day

class Day14 : Day(2021, 14) {
    override fun partOne(): Any {
        return findDeltaBetweenMostAndLeastCommonElements2(inputList, 10)
    }

    override fun partTwo(): Any {
        return 0
    }

    // idiot version
    fun findDeltaBetweenMostAndLeastCommonElements(inputList: List<String>, steps: Int): Long {
        var template = inputList.firstOrNull() ?: throw IllegalStateException()
        val substitutionMap = mutableMapOf<String,String>()

        inputList.drop(2).forEach { line ->
            Regex("(\\w+) -> (\\w)").matchEntire(line.trim())?.destructured?.let { (pattern, insertion) ->
                substitutionMap[pattern] = insertion
            }
        }

        for (step in 1..steps) {
            template = template.windowed(2).fold("") { acc, string ->
                acc + string[0] + (substitutionMap[string] ?: "")
            } + template.last()

            println(template)
        }

        val counts = mutableMapOf<Char,Long>()
        template.forEach { c ->
            counts[c] = counts.getOrDefault(c, 0) + 1
        }

        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    // the version where we keep counts of pairs
    fun findDeltaBetweenMostAndLeastCommonElements2(inputList: List<String>, steps: Int): Long {
        var template = inputList.firstOrNull() ?: throw IllegalStateException()
        val substitutionMap = mutableMapOf<String,String>()

        inputList.drop(2).forEach { line ->
            Regex("(\\w+) -> (\\w)").matchEntire(line.trim())?.destructured?.let { (pattern, insertion) ->
                substitutionMap[pattern] = insertion
            }
        }

        var pairCounts = mutableMapOf<String,Long>()
        var letterCounts = mutableMapOf<Char,Long>()
        template.windowed(2).forEach {
            pairCounts[it] = pairCounts.getOrDefault(it, 0L) + 1
        }

        template.forEach { char ->
            letterCounts[char] = letterCounts.getOrDefault(char, 0L) + 1
        }

        repeat (steps) {
            val newlyCreatePairCounts = mutableMapOf<String,Long>()

            // increment the pairs for the substitutions, first letter of pair with sub and sub with last letter of pair.
            // we're replacing a pair with two "new" pairs, so we're double counting a bit.
            pairCounts.forEach { (pair, count) ->
                letterCounts[substitutionMap[pair]!!.first()] = letterCounts.getOrDefault(substitutionMap[pair], 0L) + count
                ("" + pair[0] + substitutionMap[pair]).let { key ->
                    newlyCreatePairCounts[key] = newlyCreatePairCounts.getOrDefault(key, 0L) + count
                }
                ("" + substitutionMap[pair] + pair[1]).let { key ->
                    newlyCreatePairCounts[key] = newlyCreatePairCounts.getOrDefault(key, 0L) + count
                }
            }

            pairCounts = newlyCreatePairCounts
        }

        return with(letterCounts) {
            maxOf { it.value } - minOf { it.value }
        }
    }

}