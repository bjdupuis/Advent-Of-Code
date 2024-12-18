package days.aoc2024

import days.Day

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
        val registers = mutableMapOf<Char, Long>()
        registers['A'] = input[0].split(" ").last().toLong()
        registers['B'] = input[1].split(" ").last().toLong()
        registers['C'] = input[2].split(" ").last().toLong()

        val program = input[4].split(" ").last().split(",")
        var pc = 0
        val output = mutableListOf<String>()
        while(pc <= program.lastIndex) {
            when (program[pc]) {
                "0" -> registers['A'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "1" -> registers['B'] = registers['B']!! xor program[pc + 1].toLong()
                "2" -> registers['B'] = operandOf(program[pc + 1], registers).mod(8L)
                "3" -> if (registers['A'] != 0L) pc = (program[pc + 1].toLong() - 2L).toInt()
                "4" -> registers['B'] = registers['B']!! xor registers['C']!!
                "5" -> output.add(operandOf(program[pc + 1], registers).mod(8).toString())
                "6" -> registers['B'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "7" -> registers['C'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
            }

            pc += 2
        }

        return output.joinToString(",")
    }

    private fun operandOf(opcode: String, registers: Map<Char, Long>): Long {
        return when (opcode) {
            "0" -> 0L
            "1" -> 1L
            "2" -> 2L
            "3" -> 3L
            "4" -> registers['A']!!
            "5" -> registers['B']!!
            "6" -> registers['C']!!
            else -> throw IllegalStateException("Opcode not recognized: $opcode")
        }
    }

    /*
    Program: 2,4, 1,7, 7,5, 0,3, 4,4, 1,7, 5,5, 3,0

    B = A % 8
    B = B XOR 7
    C = A / 2 ^ B
    A = A / 8
    B = B XOR C
    OUTPUT B
    IF A != 0 GOTO 1
    */
    fun calculatePartTwo(input: List<String>): Long {
        val program = input[4].split(" ").last().split(",")
        return findSolutionForOutput(1, program, program.lastIndex)
    }

    private fun findSolutionForOutput(a: Long, program: List<String>, outputIndex: Int): Long {
        val registers = mutableMapOf<Char, Long>()

        for (i in 0..63) {
            registers['A'] = a + i
            registers['B'] = 0L
            registers['C'] = 0L
            if (runProgram(registers, program) == program.drop(outputIndex).joinToString(",")
            ) {
                if (outputIndex == 0) {
                    return a
                } else {
                    val solution = findSolutionForOutput((a + i) * 8L, program, outputIndex - 1)
                    if (solution != Long.MAX_VALUE) {
                        return solution
                    }
                }
            }
        }

        return Long.MAX_VALUE
    }

    fun runProgram(registers: MutableMap<Char, Long>, program: List<String>): String {
        var pc = 0
        val output = mutableListOf<String>()
        while(pc <= program.lastIndex) {
            when (program[pc]) {
                "0" -> registers['A'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "1" -> registers['B'] = registers['B']!! xor program[pc + 1].toLong()
                "2" -> registers['B'] = operandOf(program[pc + 1], registers).mod(8L)
                "3" -> if (registers['A'] != 0L) pc = (program[pc + 1].toLong() - 2L).toInt()
                "4" -> registers['B'] = registers['B']!! xor registers['C']!!
                "5" -> output.add(operandOf(program[pc + 1], registers).mod(8).toString())
                "6" -> registers['B'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
                "7" -> registers['C'] = registers['A']!! / Math.pow(2.0, operandOf(program[pc + 1], registers).toDouble()).toInt()
            }

            pc += 2
        }

        return output.joinToString(",")
    }
}