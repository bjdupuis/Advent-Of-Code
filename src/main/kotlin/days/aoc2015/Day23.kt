package days.aoc2015

import days.Day

class Day23 : Day(2015, 23) {
    override fun partOne(): Any {
        val computer = Computer()
        computer.execute(inputList)
        return computer.registers['b']!!
    }

    override fun partTwo(): Any {
        val computer = Computer()
        computer.registers['a'] = 1
        computer.execute(inputList)
        return computer.registers['b']!!
    }

    class Computer {
        val registers = mutableMapOf('a' to 0L, 'b' to 0L)

        private var pc = 0

        fun execute(instructions: List<String>) {
            while (pc in instructions.indices) {
                val current = instructions[pc]
                when (current.substring(0..2)) {
                    "hlf" -> {
                        registers[current.last()] = registers[current.last()]!!.div(2)
                        pc++
                    }
                    "tpl" -> {
                        registers[current.last()] = registers[current.last()]!!.times(3)
                        pc++
                    }
                    "inc" -> {
                        registers[current.last()] = registers[current.last()]!!.plus(1)
                        pc++
                    }
                    "jmp" -> {
                        Regex("jmp ([+-])([0-9]+)").matchEntire(current)?.destructured?.let { (sig, offset) ->
                            offset.toInt().let {
                                pc += if (sig == "-") -it else it
                            }
                        }
                    }
                    "jie" -> {
                        Regex("jie ([ab]), ([+-])([0-9]+)").matchEntire(current)?.destructured?.let { (register, sig, offset) ->
                            offset.toInt().let {
                                if (registers[register.first()]!! % 2 == 0L) {
                                    pc += if (sig == "-") -it else it
                                } else {
                                    pc++
                                }
                            }
                        }
                    }
                    "jio" -> {
                        Regex("jio ([ab]), ([+-])([0-9]+)").matchEntire(current)?.destructured?.let { (register, sig, offset) ->
                            offset.toInt().let {
                                if (registers[register.first()] == 1L) {
                                    pc += if (sig == "-") -it else it
                                } else {
                                    pc++
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}