package days.aoc2020

import days.Day

class Day8: Day(2020, 8) {
    override fun partOne(): Any {
        val gameConsole = GameConsole(inputList)
        val visitedInstructions = mutableSetOf<Int>()

        do {
            val accumulator = gameConsole.accumulator
            gameConsole.executeNextInstruction()
            if(!visitedInstructions.add(gameConsole.programCounter)) {
                return accumulator
            }
        } while (true)

    }

    override fun partTwo(): Any {
        val instructions = mutableListOf<String>()

        var lastChangedInstructionIndex = -1
        var accumulator: Int
        do {
            instructions.clear()
            instructions.addAll(inputList)
            for (i in lastChangedInstructionIndex + 1..instructions.size) {
                when {
                    instructions[i].startsWith("nop") -> {
                        instructions[i] = instructions[i].replace("nop", "jmp")
                        lastChangedInstructionIndex = i
                        break
                    }
                    instructions[i].startsWith("jmp") -> {
                        instructions[i] = instructions[i].replace("jmp", "nop")
                        lastChangedInstructionIndex = i
                        break
                    }
                }
            }
            val gameConsole = GameConsole(instructions)
            val visitedInstructions = mutableSetOf<Int>()

            do {
                gameConsole.executeNextInstruction()
            } while (gameConsole.programCounter in instructions.indices && visitedInstructions.add(gameConsole.programCounter))

            accumulator = gameConsole.accumulator
        } while (gameConsole.programCounter in inputList.indices)

        return accumulator
    }

    class GameConsole(private val instructions: List<String>) {
        var accumulator = 0
        var programCounter = 0

        fun executeNextInstruction() {
            val instruction = instructions[programCounter]
            when {
                instruction.startsWith("nop") ->
                    programCounter++
                instruction.startsWith("jmp") -> {
                    Regex("([+-])(\\d+)").find(instruction)?.destructured?.let { (direction, offset) ->
                        if (direction == "-") {
                            programCounter -= offset.toInt()
                        } else {
                            programCounter += offset.toInt()
                        }
                    }
                }
                instruction.startsWith("acc") -> {
                    Regex("([+-])(\\d+)").find(instruction)?.destructured?.let { (sign, value) ->
                        if (sign == "-") {
                            accumulator -= value.toInt()
                        } else {
                            accumulator += value.toInt()
                        }
                    }
                    programCounter++
                }
            }
        }
    }
}