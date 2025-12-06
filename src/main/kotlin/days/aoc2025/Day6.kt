package days.aoc2025

import days.Day
import util.CharArray2d
import util.transpose
import kotlin.collections.reduce
import kotlin.collections.sum

class Day6 : Day(2025, 6) {
    override fun partOne(): Any {
        return calculatePartOne(4, inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(4, inputList)
    }

    fun calculatePartOne(rowsOfOperands: Int, input: List<String>): Long {
        val values = input.take(rowsOfOperands).map { line-> line.split(Regex("\\s+")).filter { it.isNotEmpty() }.map { it.trimEnd().toLong() } }.transpose()
        val operators = input.drop(rowsOfOperands).take(1).map { it.split(Regex("\\s+")).filter { it.isNotEmpty() } }.first()

        return operators.mapIndexed { index, operator ->
            when (operator) {
                "+" -> values[index].sum()
                "*" -> values[index].reduce { acc, i -> i * acc }
                else -> throw IllegalStateException("Got a weird operator: $operator")
            }
        }.sum()
    }

    fun calculatePartTwo(rowsOfOperands: Int, input: List<String>): Long {
        val values = CharArray2d(input.take(rowsOfOperands))
        val operators = input.drop(rowsOfOperands).take(1).map { it.split(Regex("\\s+")).filter { it.isNotEmpty() } }.first()

        var currentColumn = 0
        return operators.mapIndexed { index, operator ->
            val operands = mutableListOf<Long>()
            var currentOperand: String? = null
            while (currentColumn <= values.maxColumnIndex) {
                currentOperand = String(values.getColumn(currentColumn++)).trim()
                if (currentOperand.isNotEmpty()) {
                    operands.add(currentOperand.toLong())
                } else {
                    break
                }
            }
            when (operator) {
                "+" -> operands.sum()
                "*" -> operands.reduce { acc, i -> i * acc }
                else -> throw IllegalStateException("Got a weird operator: $operator")
            }
        }.sum()
    }
}