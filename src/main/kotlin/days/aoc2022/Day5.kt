package days.aoc2022

import days.Day
import java.util.*

class Day5 : Day(2022, 5) {
    override fun partOne(): Any {
        return determineTopOfStacks(inputList, false)
    }

    override fun partTwo(): Any {
        return determineTopOfStacks(inputList, true)
    }

    private fun determineTopOfStacks(input: List<String>, useTemporaryStack: Boolean): String {
        val stacks = mutableListOf<Stack<Char>>()

        input.takeWhile { it.isNotEmpty() }.let { stackDescription ->
            val numberOfStacks = stackDescription.last().last().toString().toInt()
            println("Number of stacks is $numberOfStacks")
            repeat(numberOfStacks) {
                stacks.add(Stack())
            }
            //  1   5   9   13...
            // [N] [M] [F] [D] [R] [C] [W] [T] [M]
            stackDescription.reversed().drop(1).forEach { line ->
                for (i in 1..numberOfStacks) {
                    val index = (i - 1) * 4 + 1
                    if (line.indices.contains(index)) {
                        line[index].let { crate ->
                            if (crate != ' ') {
                                stacks[i - 1].push(crate)
                            }
                        }
                    }
                }
            }
        }

        // move 1 from 2 to 1
        input.dropWhile { it.isNotEmpty() }
            .forEach { line ->
                Regex("move (\\d+) from (\\d) to (\\d)").matchEntire(line)?.destructured?.let { (count, source, dest) ->
                    val tempStack = Stack<Char>()
                    repeat(count.toInt()) {
                        if (useTemporaryStack) {
                            tempStack.push(stacks[source.toInt() - 1].pop())
                        } else {
                            stacks[dest.toInt() - 1].push(stacks[source.toInt() - 1].pop())
                        }
                    }
                    if (useTemporaryStack) {
                        repeat(count.toInt()) {
                            stacks[dest.toInt() - 1].push(tempStack.pop())
                        }
                    }
                }
            }

        return stacks.map { it.peek() }.joinToString(separator = "")
    }
}