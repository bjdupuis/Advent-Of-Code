package days.aoc2024

import days.Day
import kotlin.math.pow

class Day17 : Day(2024, 17) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    /*
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
     */
    fun calculatePartOne(input: List<String>): String {
        val registers = mutableMapOf<Char, Int>()
        registers['A'] = input[0].split(" ").last().toInt()
        registers['B'] = input[1].split(" ").last().toInt()
        registers['C'] = input[2].split(" ").last().toInt()

        val program = input[4].split(" ").last().split(",")
        var pc = 0
        val output = mutableListOf<String>()
        while(pc <= program.lastIndex) {
            when (program[pc]) {
                "0" -> registers['A'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "1" -> registers['B'] = registers['B']!! xor program[pc + 1].toInt()
                "2" -> registers['B'] = operandOf(program[pc + 1], registers).mod(8)
                "3" -> if (registers['A'] != 0) pc = program[pc + 1].toInt() - 2
                "4" -> registers['B'] = registers['B']!! xor registers['C']!!
                "5" -> output.add(operandOf(program[pc + 1], registers).mod(8).toString())
                "6" -> registers['B'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "7" -> registers['C'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
            }

            pc += 2
        }

        return output.joinToString(",")
    }

    private fun operandOf(opcode: String, registers: Map<Char, Int>): Int {
        return when (opcode) {
            "0" -> 0
            "1" -> 1
            "2" -> 2
            "3" -> 3
            "4" -> registers['A']!!
            "5" -> registers['B']!!
            "6" -> registers['C']!!
            else -> throw IllegalStateException("Opcode not recognized: $opcode")
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        var currentRegisterA = 1
        val registers = mutableMapOf<Char, Int>()
        registers['A'] = currentRegisterA
        registers['B'] = 0
        registers['C'] = 0

        val program = input[4].split(" ").last().split(",")
        while(true) {
            registers['A'] = currentRegisterA
            registers['B'] = 0
            registers['C'] = 0
            if (runProgram(registers, program) != null) {
                return currentRegisterA
            }
            currentRegisterA++
        }
    }

    private fun runProgram(registers: MutableMap<Char,Int>, program: List<String>): List<String>? {
        var pc = 0
        val output = mutableListOf<String>()
        while(pc <= program.lastIndex) {
            when (program[pc]) {
                "0" -> registers['A'] = registers['A']!! / 2.0.pow(operandOf(program[pc + 1], registers).toDouble()).toInt()
                "1" -> registers['B'] = registers['B']!! xor program[pc + 1].toInt()
                "2" -> registers['B'] = operandOf(program[pc + 1], registers).mod(8)
                "3" -> if (registers['A'] != 0) pc = program[pc + 1].toInt() - 2
                "4" -> registers['B'] = registers['B']!! xor registers['C']!!
                "5" -> {
                    operandOf(program[pc + 1], registers).mod(8).toString().let {
                        // are we replicating the program? If not, bail now
                        if (it == program[output.size]) {
                            output.add(operandOf(program[pc + 1], registers).mod(8).toString())
                        } else {
                            return null
                        }
                    }
                }
                "6" -> registers['B'] = registers['A']!! / 2.0.pow(operandOf(program[pc + 1], registers).toDouble()).toInt()
                "7" -> registers['C'] = registers['A']!! / 2.0.pow(operandOf(program[pc + 1], registers).toDouble()).toInt()
            }

            pc += 2
        }

        return if (output.joinToString(",") == program.joinToString(",")) output else null
    }
}