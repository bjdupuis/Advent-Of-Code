package days.aoc2022

import days.Day
import kotlin.math.abs

class Day20 : Day(2022, 20) {
    override fun partOne(): Any {
        return calculateGroveCoordinateSum(inputList)
    }

    override fun partTwo(): Any {
        return 0
    }

    fun calculateGroveCoordinateSumWithDecryptionKey(input: List<String>): Long {
        var first: Value<Long>? = null
        var previous: Value<Long>? = null
        var current: Value<Long>? = null
        val mixList = mutableListOf<Value<Long>>()
        input.filter { it.isNotEmpty() }.map { it.toLong() * 811589153L }.forEach { value ->
            current = Value(value, previous, null)
            mixList.add(current!!)
            if (first == null) {
                first = current
            }
            previous?.next = current
            previous = current
        }
        current?.next = first
        first?.previous = current

        val listSize = input.count { it.isNotEmpty() }

        repeat(10) {
            var current = first
            do {
                print("${current!!.value} ")
                current = current!!.next
            } while (current != first)
            println()

            mixList.forEach { toMove ->
                if (toMove.value > 0L) {
                    toMove.next!!.previous = toMove.previous
                    toMove.previous!!.next = toMove.next

                    //val moveCount = (toMove.value % listSize).toInt()
                    val moveCount = toMove.value
                    var insertAfter = toMove
                    for (i in 1.. moveCount) {
                        insertAfter = insertAfter.next!!
                    }

                    toMove.previous = insertAfter
                    toMove.next = insertAfter!!.next
                    insertAfter!!.next = toMove
                    toMove.next!!.previous = toMove
                } else if (toMove.value < 0L) {
                    toMove.next!!.previous = toMove.previous
                    toMove.previous!!.next = toMove.next

                    //val moveCount = (abs(toMove.value) % listSize).toInt()
                    var insertBefore = toMove
                    for (i in 0 downTo toMove.value) {
                        insertBefore = insertBefore.previous!!
                    }

                    toMove.next = insertBefore
                    toMove.previous = insertBefore.previous
                    insertBefore.previous = toMove
                    toMove.previous!!.next = toMove
                }
            }
        }

        var zero = first!!
        while (zero.value != 0L) {
            zero = zero.next!!
        }

        return getSequence(zero).drop(999).first()!!.value +
                getSequence(zero).drop(1999).first()!!.value +
                getSequence(zero).drop(2999).first()!!.value
    }

    fun calculateGroveCoordinateSum(input: List<String>): Int {
        var first: Value<Int>? = null
        var previous: Value<Int>? = null
        var current: Value<Int>? = null
        val mixList = mutableListOf<Value<Int>>()
        input.filter { it.isNotEmpty() }.map { it.toInt() }.forEach { value ->
            current = Value(value, previous, null)
            mixList.add(current!!)
            if (first == null) {
                first = current
            }
            previous?.next = current
            previous = current
        }
        current?.next = first
        first?.previous = current

        mixList.forEach { toMove ->
            if (toMove.value > 0) {
                toMove.next!!.previous = toMove.previous
                toMove.previous!!.next = toMove.next

                var insertAfter = toMove.next
                repeat(toMove.value - 1) {
                    insertAfter = insertAfter!!.next
                }

                toMove.previous = insertAfter
                toMove.next = insertAfter!!.next
                insertAfter!!.next = toMove
                toMove.next!!.previous = toMove
            } else if (toMove.value < 0) {
                toMove.next!!.previous = toMove.previous
                toMove.previous!!.next = toMove.next

                var insertBefore = toMove.previous
                repeat(abs(toMove.value) - 1) {
                    insertBefore = insertBefore!!.previous
                }

                toMove.next = insertBefore
                toMove.previous = insertBefore!!.previous
                insertBefore!!.previous = toMove
                toMove.previous!!.next = toMove
            }
        }

        var zero = first!!
        while (zero.value != 0) {
            zero = zero.next!!
        }

        return getSequence(zero).drop(999).first()!!.value +
                getSequence(zero).drop(1999).first()!!.value +
                getSequence(zero).drop(2999).first()!!.value

    }

    class Value<T>(val value: T, var previous: Value<T>?, var next: Value<T>?)

    private fun <T> getSequence(zero: Value<T>) = sequence {
        var current = zero
        while(true) {
            yield(current.next)
            current = current.next!!
        }
    }
}