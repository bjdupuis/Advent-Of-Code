package days.aoc2022

import days.Day

class Day21 : Day(2022, 21) {
    override fun partOne(): Any {
        return calculateWhatRootYells(inputList.filter { it.isNotBlank() })
    }

    override fun partTwo(): Any {
        return calculateWhatHumanYells(inputList.filter { it.isNotBlank() })
    }

    fun calculateWhatHumanYells(input: List<String>): Long {
        val monkeyMap = mutableMapOf<String, Monkey>()
        input.forEach { line ->
            line.split(": ").let {
                if (it[1].length > 5) {
                    Regex("(\\w+) ([+\\-*/]) (\\w+)").matchEntire(it[1])?.destructured?.let { (monkey1, operation, monkey2) ->
                        if (it[0] == "root") {
                            monkeyMap[it[0]] = MonkeyOperation(monkey1, monkey2) { o1, o2 -> o1 - o2 }
                        } else {
                            monkeyMap[it[0]] = MonkeyOperation(monkey1, monkey2, when (operation) {
                                "-" -> { o1, o2 -> o1 - o2 }
                                "+" -> { o1, o2 -> o1 + o2 }
                                "/" -> { o1, o2 -> o1 / o2 }
                                "*" -> { o1, o2 -> o1 * o2 }
                                else -> throw IllegalStateException()
                            })
                        }
                    }
                } else {
                    monkeyMap[it[0]] = MonkeyYell(it[1].toLong())
                }
            }
        }

        val root = monkeyMap["root"]!!

        var top = 500000000000000L
        var bottom = 0L
        while (true) {
            val index = bottom + (top - bottom) / 2

            monkeyMap["humn"] = MonkeyYell(index)

            val result = calculateValue(root, monkeyMap)
            if (result == 0L) {
                return index
            } else if (result > 0L) {
                bottom += (top - bottom) / 2
            } else if (result < 0L) {
                top -= (top - bottom) / 2
            }
        }

        throw IllegalStateException()
    }

    fun calculateWhatRootYells(input: List<String>): Long {
        val monkeyMap = mutableMapOf<String, Monkey>()
        input.forEach { line ->
            line.split(": ").let {
                if (it[1].length > 5) {
                    Regex("(\\w+) ([+\\-*/]) (\\w+)").matchEntire(it[1])?.destructured?.let { (monkey1, operation, monkey2) ->
                        monkeyMap[it[0]] = MonkeyOperation(monkey1, monkey2, when (operation) {
                            "-" -> { o1, o2 -> o1 - o2 }
                            "+" -> { o1, o2 -> o1 + o2 }
                            "/" -> { o1, o2 -> o1 / o2 }
                            "*" -> { o1, o2 -> o1 * o2 }
                            else -> throw IllegalStateException()
                        })
                    }
                } else {
                    monkeyMap[it[0]] = MonkeyYell(it[1].toLong())
                }
            }
        }

        val root = monkeyMap["root"]!!
        return calculateValue(root, monkeyMap)
    }

    private fun calculateValue(current: Monkey, monkeyMap: Map<String, Monkey>): Long {
        try {
            if (current is MonkeyYell) {
                return current.number
            } else if (current is MonkeyOperation) {
                return current.operation.invoke(
                    calculateValue(
                        monkeyMap[current.monkey1Name]!!,
                        monkeyMap
                    ), calculateValue(monkeyMap[current.monkey2Name]!!, monkeyMap)
                )
            }
        } catch (e: Exception) {
            throw IllegalStateException()
        }

        throw IllegalStateException()
    }

    open class Monkey

    class MonkeyYell(val number: Long) : Monkey()

    class MonkeyOperation(val monkey1Name: String, val monkey2Name: String, val operation: (Long,Long) -> Long) : Monkey()
}