package days.aoc2021

import days.Day

class Day24 : Day(2021, 24) {
    override fun partOne(): Any {
        return findLargestValidModelNumber(ALU(inputList))
    }

    override fun partTwo(): Any {
        return 0
    }

    fun findLargestValidModelNumber(alu: ALU): String {
        var current = "99999999999999".toCharArray()

        for (index in current.indices) {
            var min = Int.MAX_VALUE
            var minValue = '9'
            for (value in '9' downTo '1') {
                current[index] = value
                alu.reset()
                alu.processInstructionsWithInput(current.joinToString(""))
                if (alu.registers["z"]!! < min) {
                    min = alu.registers["z"]!!
                    minValue = value
                }
            }
            current[index] = minValue
        }

        alu.reset()
        alu.processInstructionsWithInput(current.joinToString(""))

        return current.joinToString("")
    }

    private fun decrement(current: String): String {
        var value = current.toCharArray()
        var index = value.lastIndex
        while(index > 0) {
            if (current[index] == '1') {
                value[index] = '9'
                index--
            }
            else {
                value[index]--
                break
            }
        }
        return value.joinToString("")
    }

    class ALU(private val instructions: List<String>) {
        val registers = mutableMapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)

        fun processInstructionsWithInput(input: String): ALU {
            var remainingInput = input.trim()
            var pc = 0
            while (pc <= instructions.lastIndex) {
                val instruction = instructions[pc++].trim()
                when (instruction.take(3)) {
                    "inp" -> {
                        registers[instruction.last().toString()] = remainingInput.first() - '0'
                        remainingInput = remainingInput.drop(1)
                    }
                    else -> {
                        val (operand1, operand2) = instruction.drop(4).split(" ".toRegex())
                        when(instruction.take(3)) {
                            "add" -> {
                                if (registers.keys.contains(operand2)) {
                                    registers[operand1] = registers[operand1]!!.plus(registers[operand2]!!)
                                } else {
                                    registers[operand1] = registers[operand1]!!.plus(operand2.toInt())
                                }
                            }
                            "mul" -> {
                                if (registers.keys.contains(operand2)) {
                                    registers[operand1] = registers[operand1]!!.times(registers[operand2]!!)
                                } else {
                                    registers[operand1] = registers[operand1]!!.times(operand2.toInt())
                                }
                            }
                            "div" -> {
                                if (registers.keys.contains(operand2)) {
                                    registers[operand1] = registers[operand1]!!.div(registers[operand2]!!)
                                } else {
                                    registers[operand1] = registers[operand1]!!.div(operand2.toInt())
                                }
                            }
                            "mod" -> {
                                if (registers.keys.contains(operand2)) {
                                    registers[operand1] = registers[operand1]!!.mod(registers[operand2]!!)
                                } else {
                                    registers[operand1] = registers[operand1]!!.mod(operand2.toInt())
                                }
                            }
                            "eql" -> {
                                if (registers.keys.contains(operand2)) {
                                    registers[operand1] = if (registers[operand1]!! == registers[operand2]!!) 1 else 0
                                } else {
                                    registers[operand1] = if (registers[operand1]!! == operand2.toInt()) 1 else 0
                                }
                            }
                        }
                    }
                }
            }
            return this
        }

        fun reset() {
            registers["w"] = 0
            registers["x"] = 0
            registers["y"] = 0
            registers["z"] = 0
        }
    }
}