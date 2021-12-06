package days.aoc2020

import days.Day
import java.util.function.BinaryOperator
import java.util.function.LongBinaryOperator
import kotlin.collections.ArrayDeque

class Day18: Day(2020, 18) {
    override fun partOne(): Any {
        return inputList.map {
            calculateSimple(it).first
        }.sum()
    }

    private fun calculateSimple(input: String): Pair<Long,Int> {
        var i = 0
        var operand: Long? = null
        var oper: ((Long,Long) -> Long)? = null

        while (i in input.indices) {
            when(input[i]) {
                in '0'..'9' -> {
                    if (operand == null) {
                        operand = (input[i] - '0').toLong()
                    } else {
                        operand = oper?.invoke(operand,(input[i] - '0').toLong()) ?: error("oops")
                        oper = null
                    }
                }
                '(' -> {
                    val (intermediateResult, newIndex) = calculateSimple(input.substring(i+1))
                    i += newIndex + 1
                    operand = oper?.invoke(operand!!,intermediateResult) ?: intermediateResult
                    oper = null
                }
                ')' -> {
                    return Pair(operand!!, i)
                }
                '+' -> {
                    oper = Long::plus
                }
                '*' -> {
                    oper = Long::times
                }
            }
            i++
        }

        return Pair(operand!!,i)
    }

    override fun partTwo(): Any {
        return calculatePart2(inputList)
    }

    fun calculatePart2(input: List<String>): Long {
        return input.map {
            calculateAdvanced(it).first
        }.sum()
    }

    private fun calculateAdvanced(input: String): Pair<Long,Int> {
        var i = 0
        var stack: ArrayDeque<Any> = ArrayDeque()
        val stackUnroller = {
            while (stack.size > 1) {
                val operand1 = stack.removeLast() as Long
                val operator = stack.removeLast() as LongArithmetics
                val operand2 = stack.removeLast() as Long
                stack.addLast(operator.apply(operand1, operand2))
            }
            Pair(stack.removeFirst() as Long, i)
        }

        while (i in input.indices) {
            when(input[i]) {
                in '0'..'9' -> {
                    val newOperand = (input[i] - '0').toLong()
                    if (stack.isNotEmpty() && stack.first() == LongArithmetics.PLUS) {
                        val operator = stack.removeFirst() as LongArithmetics
                        val operand = stack.removeFirst() as Long
                        stack.addFirst(operator.apply(operand, newOperand))
                    } else {
                        stack.addFirst(newOperand)
                    }
                }
                '(' -> {
                    val (intermediateResult, newIndex) = calculateAdvanced(input.substring(i+1))
                    i += newIndex + 1
                    if (stack.isNotEmpty() && stack.first() == LongArithmetics.PLUS) {
                        val operator = stack.removeFirst() as LongArithmetics
                        val operand = stack.removeFirst() as Long
                        stack.addFirst(operator.apply(operand, intermediateResult))
                    } else {
                        stack.addFirst(intermediateResult)
                    }
                }
                ')' -> {
                    return stackUnroller()
                }
                '+' -> {
                    stack.addFirst(LongArithmetics.PLUS)
                }
                '*' -> {
                    stack.addFirst(LongArithmetics.TIMES)
                }
            }
            i++
        }

        return stackUnroller()
    }

    enum class LongArithmetics : BinaryOperator<Long>, LongBinaryOperator {
        PLUS {
            override fun apply(t: Long, u: Long): Long = t + u
        },
        TIMES {
            override fun apply(t: Long, u: Long): Long = t * u
        };
        override fun applyAsLong(t: Long, u: Long) = apply(t, u)
    }}
