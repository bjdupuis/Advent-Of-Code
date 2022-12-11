package days.aoc2022

import days.Day

class Day11 : Day(2022, 11) {
    override fun partOne(): Any {
        return calculateTopTwoMonkeysMonkeyBusinessAfterRounds(inputList, 20, true)
    }

    override fun partTwo(): Any {
        return calculateTopTwoMonkeysMonkeyBusinessAfterRounds(inputList, 10000, false)
    }



    fun calculateTopTwoMonkeysMonkeyBusinessAfterRounds(input: List<String>, rounds: Int, decreaseWorryLevel: Boolean): Long {

        val monkeyBusiness = parseMonkeyBusiness(input, decreaseWorryLevel)

        repeat(rounds) {
            monkeyBusiness.monkeys.forEach { monkey ->
                monkey.doMonkeyThings()
            }
        }

        monkeyBusiness.monkeys.forEachIndexed { i, monkey -> println("Monkey $i looked at ${monkey.itemsInspected} items") }

        return monkeyBusiness.monkeys.map { it.itemsInspected.toLong() }.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }

    private fun parseMonkeyBusiness(input: List<String>, decreaseWorryLevel: Boolean): MonkeyBusiness {
        var index = 0
        val monkeyBusiness = MonkeyBusiness(decreaseWorryLevel)
        var moduloFactor = 1

        while (index in input.indices) {
            index++ // skip monkey "name"
            val itemList = input[index++].split(": ").last().split(", ").map { it.toLong() }
            val operation = input[index++].split("  Operation: new = old ").last().split(" ")?.let { parts ->
                Pair(parts[0], parts[1])
            }
            val divisor = input[index++].split(" ").last().toInt()
            moduloFactor *= divisor
            val trueDestination = input[index++].split(" ").last().toInt()
            val falseDestination = input[index++].split(" ").last().toInt()
            index++ // blank

            monkeyBusiness.addMonkey(itemList, { worryLevel: Long ->
                val operand = if (operation?.second == "old") worryLevel else operation?.second?.toLong() ?: throw IllegalStateException()
                when (operation?.first) {
                    "+" -> worryLevel + operand
                    "*" -> worryLevel * operand
                    else -> throw IllegalStateException()
                }
            }, { worryLevel: Long ->
                if (worryLevel % divisor == 0L) {
                    trueDestination
                } else {
                    falseDestination
                }
            } )
        }
        monkeyBusiness.moduloFactor = moduloFactor
        return monkeyBusiness
    }

    class MonkeyBusiness(private val decreaseWorryLevel: Boolean) {
        val monkeys = mutableListOf<Monkey>()
        var moduloFactor: Int = 1

        fun throwToMonkey(item: Long, monkeyIndex: Int) {
            monkeys[monkeyIndex].items.add(item)
        }

        fun addMonkey(items: List<Long>, operation: (Long) -> Long, determineDestination: (Long) -> Int) {
            val monkey = Monkey(operation, determineDestination)
            monkey.items.addAll(items)
            monkeys.add(monkey)
        }

        inner class Monkey(
            private val operation: (Long) -> Long,
            private val determinateDestination: (Long) -> Int
        ) {
            val items = mutableListOf<Long>()
            var itemsInspected = 0

            fun doMonkeyThings() {
                while(items.isNotEmpty()) {
                    itemsInspected++
                    val item = items.removeAt(0)
                    val newWorryLevel: Long = operation.invoke(item).let { if (decreaseWorryLevel) it / 3 else it % moduloFactor }
                    throwToMonkey(newWorryLevel, determinateDestination(newWorryLevel))
                }
            }
        }
    }
}