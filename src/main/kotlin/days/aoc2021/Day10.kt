package days.aoc2021

import days.Day

class Day10 : Day(2021, 10) {
    override fun partOne(): Any {
        return calculateTotalSyntaxError(inputList)
    }

    override fun partTwo(): Any {
        return calculateMiddleCompleteScore(inputList)
    }

    fun calculateMiddleCompleteScore(inputList: List<String>): Long {
        val scores = mutableListOf<Long>()

        inputList.forEach { line ->
            try {
                scores.add(calculateMissingScore(line))
            } catch (invalidSyntaxException: InvalidSyntaxException) {
                // empty
            }
        }

        return scores.sorted()[(scores.size / 2)]
    }

    // [({(<(())[]>[[{[]{<()<>>
    private fun calculateMissingScore(line: String): Long {
        val delimiterMap = mapOf('<' to '>', '(' to ')', '[' to ']', '{' to '}')
        val scores = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

        val stack = ArrayDeque<Char>()
        line.forEach { c ->
            if (c in delimiterMap.keys) {
                stack.addFirst(c)
            } else {
                if (stack.isEmpty() || delimiterMap[stack.first()] != c) {
                    throw InvalidSyntaxException()
                } else {
                    stack.removeFirst()
                }
            }
        }

        // we're left with the remaining things in the stack
        var sum = 0L
        while (stack.isNotEmpty()) {
            sum = sum * 5 + scores[stack.removeFirst()]!!
        }

        return sum
    }


    fun calculateTotalSyntaxError(inputLines: List<String>): Int {
        return inputLines.sumBy { line ->
            calculateSyntaxError(line)
        }
    }

    private fun calculateSyntaxError(line: String): Int {
        val delimiterSyntaxScoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        val delimiterMap = mapOf('<' to '>', '(' to ')', '[' to ']', '{' to '}')

        val stack = ArrayDeque<Char>()
        line.forEach { c ->
            if (c in delimiterMap.keys) {
                stack.addFirst(c)
            } else {
                if (stack.isEmpty() || delimiterMap[stack.first()] != c) {
                    return delimiterSyntaxScoreMap[c]!!
                } else {
                    stack.removeFirst()
                }
            }
        }

        return 0
    }

    class InvalidSyntaxException : Exception()
}