package days.aoc2022

import days.Day

class Day10 : Day(2022, 10) {
    override fun partOne(): Any {
        return calculcateSumOfInterestingSignalStrengths(inputList)
    }

    override fun partTwo(): Any {
        val output = rasterizeInputProgram(inputList)
        output.forEach { println(it) }
        return output
    }

    fun calculcateSumOfInterestingSignalStrengths(input: List<String>): Int {
        val rasterizer = Rasterizer()

        input.forEach { instr -> rasterizer.parseInstruction(instr)}

        return rasterizer.sum
    }

    fun rasterizeInputProgram(input: List<String>): List<String> {
        val rasterizer = Rasterizer()

        input.forEach { instr -> rasterizer.parseInstruction(instr)}

        return rasterizer.output
    }

    class Rasterizer {
        var cycle = 1
        var x = 1
        var sum = 0
        val output = mutableListOf<String>()
        var currentLine = StringBuilder()
        private val interestingCycles = listOf(20, 60, 100, 140, 180, 220)

        fun parseInstruction(instr: String) {
            when {
                instr == "noop" -> {
                    processInstruction(1) {}
                }
                instr.startsWith("addx") -> {
                    val increment = instr.split(" ").last().toInt()
                    processInstruction(2) { x += increment }
                }
            }
        }

        private fun processInstruction(cycles: Int, operation: () -> Unit) {
            repeat(cycles) {
                rasterize()
                cycle++
            }
            operation.invoke()
        }

        private fun rasterize() {
            currentLine.append(if ((cycle - 1) % 40 in pixel()) '#' else '.')

            if (cycle % 40 == 0) {
                output.add(currentLine.toString())
                currentLine = StringBuilder()
            }

            if (cycle in interestingCycles) {
                sum += x * cycle
            }
        }

        private fun pixel(): List<Int> { return listOf(x - 1, x, x + 1) }
    }
}