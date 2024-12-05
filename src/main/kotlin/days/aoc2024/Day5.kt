package days.aoc2024

import days.Day

class Day5 : Day(2024, 5) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val pageRules = mutableMapOf<Int,MutableList<Int>>()

        input.takeWhile {
            it.isNotEmpty()
        }.forEach {
            val pairs = it.split("|").map(String::toInt)
            val list = pageRules.getOrPut(pairs[0] ) { mutableListOf() }
            list.add(pairs[1])
        }

        return input.dropWhile { it.isNotEmpty() }.drop(1).sumOf {
            val pages = it.split(",").map(String::toInt)
            if (isPagesAreValid(pageRules, pages)) {
                pages[pages.size / 2]
            } else {
                0
            }
        }
    }

    private fun isPagesAreValid(rules: Map<Int,List<Int>>, pages: List<Int>): Boolean {
        for (rule in rules) {
            if (pages.contains(rule.key) && rule.value.intersect(pages.takeWhile { it != rule.key }).isNotEmpty()) {
                return false
            }
        }
        return true
    }

    fun calculatePartTwo(input: List<String>): Int {
        val pageRules = mutableMapOf<Int,MutableList<Int>>()

        input.takeWhile {
            it.isNotEmpty()
        }.forEach {
            val pairs = it.split("|").map(String::toInt)
            val list = pageRules.getOrPut(pairs[0] ) { mutableListOf() }
            list.add(pairs[1])
        }

        return input.dropWhile { it.isNotEmpty() }.drop(1).sumOf {
            val pages = it.split(",").map(String::toInt)
            if (!isPagesAreValid(pageRules, pages)) {
                fixedPagesOf(pageRules, pages)[pages.size / 2]
            } else {
                0
            }
        }
    }

    private fun fixedPagesOf(rules: Map<Int,List<Int>>, pages: List<Int>): List<Int> {
        val result = pages.toMutableList()
        do {
            for (rule in rules) {
                if (result.contains(rule.key)) {
                    val intersection = rule.value.intersect(result.takeWhile { it != rule.key })

                    if (intersection.isNotEmpty()) {
                        val index = result.indexOf(intersection.first())
                        result[result.indexOf(rule.key)] = intersection.first()
                        result[index] = rule.key
                    }
                }
            }
        } while (!isPagesAreValid(rules, result))

        return result
    }
}