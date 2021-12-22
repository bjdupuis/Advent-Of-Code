package days.aoc2021

import days.Day
import kotlin.math.ceil

class Day18 : Day(2021, 18) {
    override fun partOne(): Any {
        return calculateMagnitudeOfFinalSum(inputList)
    }

    override fun partTwo(): Any {
        return findLargestMagnitudeOfPairs(inputList)
    }

    fun findLargestMagnitudeOfPairs(inputList: List<String>): Int {
        val magnitudes = mutableListOf<Int>()
        for (i in 0 until inputList.lastIndex) {
            for (j in i+1..inputList.lastIndex) {
                magnitudes.add(parseSnailfishNumber(inputList[i]).plus(parseSnailfishNumber(inputList[j])).reduce().magnitude())
                magnitudes.add(parseSnailfishNumber(inputList[j]).plus(parseSnailfishNumber(inputList[i])).reduce().magnitude())
            }
        }

        return magnitudes.maxOf { it }
    }

    fun calculateMagnitudeOfFinalSum(inputList: List<String>): Int {
        return addSnailfishNumbersTogether(inputList).magnitude()
    }

    fun addSnailfishNumbersTogether(inputList: List<String>): SnailfishNumber {
        return inputList.map { line ->
            parseSnailfishNumber(line)
        }.reduce { acc, snailfishNumber ->
            acc.plus(snailfishNumber).reduce()
        }
    }

    fun parseSnailfishNumber(string: String): SnailfishNumber {
        var depth = 0
        var current = string

        if (current.first() in '0'..'9') {
            return SnailfishNumber.Regular(current.first() - '0')
        }

        // we want to find the comma separating the left from the right of the outermost enclosing
        // snailfish number
        while (current.isNotEmpty()) {
            when (current.first()) {
                '[' -> {
                    depth++
                }
                ']' -> {
                    depth--
                }
                ',' -> {
                    if (depth == 1) {
                        break
                    }
                }
            }
            current = current.drop(1)
        }

        val left = string.substring(1..string.length - current.length)
        val right = current.drop(1).dropLast(1)
        return SnailfishNumber.Pair(parseSnailfishNumber(left), parseSnailfishNumber(right))
    }

    open class SnailfishNumber(var parent: Pair? = null) {

        data class Pair(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber() {
            init {
                left.parent = this
                right.parent = this
            }
        }

        data class Regular(var value: Int) : SnailfishNumber()

        fun plus(other: SnailfishNumber): SnailfishNumber {
            return Pair(this, other)
        }

        fun explode(toExplode: SnailfishNumber) {
            val pair = toExplode as Pair
            var current: SnailfishNumber? = pair

            // find first left regular to add this left to
            while (current?.parent != null) {
                if (current?.parent?.left != current) {
                    // found it, now we have to find the regular all the way to the right
                    current = current?.parent?.left
                    while (current is Pair && current?.right != null) {
                        current = current.right
                    }

                    if (current != null && current is Regular) {
                        current.value += (pair.left as Regular).value
                        break
                    }
                } else {
                    current = current?.parent
                }
            }

            // find first right regular to add this right to
            current = pair
            while (current?.parent != null) {
                if (current?.parent?.right != current) {
                    // found it, now we have to find the regular all the way to the left
                    current = current?.parent?.right
                    while (current is Pair && current?.left != null) {
                        current = current.left
                    }

                    if (current != null && current is Regular) {
                        current.value += (pair.right as Regular).value
                        break
                    }
                } else {
                    current = current?.parent
                }
            }

            // now replace the value
            val replacement = Regular(0)
            replacement.parent = toExplode.parent
            if (toExplode.parent?.left === toExplode) {
                toExplode.parent?.left = replacement
            } else {
                toExplode.parent?.right = replacement
            }
        }

        private fun findFirstPairToExplode(number: SnailfishNumber, depth: Int): SnailfishNumber? {
            return if (number is Pair) {
                if (depth == 4) {
                    number
                } else {
                    findFirstPairToExplode(number.left, depth + 1) ?: findFirstPairToExplode(number.right, depth + 1)
                }
            } else {
                null
            }
        }

        private fun findFirstValueToSplit(number: SnailfishNumber): Regular? {
            if (number is Pair) {
                return findFirstValueToSplit(number.left) ?: findFirstValueToSplit(number.right)
            } else if (number is Regular) {
                if (number.value > 9) {
                    return number
                }
            }

            return null
        }

        private fun split(number: Regular) {
            val newValue = Pair(Regular(number.value / 2), Regular(ceil(number.value / 2F).toInt()))
            newValue.parent = number.parent
            if (number.parent?.left == number) {
                number.parent?.left = newValue
            } else {
                number.parent?.right = newValue
            }
        }

        fun reduce(): SnailfishNumber {
            run breaker@ {
                do {
                    run loop@{
                        findFirstPairToExplode(this, 0)?.let {
                            explode(it)
                            return@loop
                        }

                        findFirstValueToSplit(this)?.let {
                            split(it)
                            return@loop
                        } ?: return@breaker
                    }
                } while(true)
            }

            return this
        }

        fun magnitude(): Int {
            if (this is Pair) {
                return left.magnitude() * 3 + right.magnitude() * 2
            } else if (this is Regular) {
                return value
            }

            throw IllegalStateException()
        }

    }
}