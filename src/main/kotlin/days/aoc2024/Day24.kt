package days.aoc2024

import days.Day

class Day24 : Day(2024, 24) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    abstract class Pin(val name: String, val pins: Map<String, Pin>) {
        abstract fun output(): Int
    }

    class ConstantPin(name: String, private val value: Int, pins: Map<String, Pin>): Pin(name, pins) {
        override fun output(): Int {
            return value
        }
    }

    class VariablePin(name: String, pins: Map<String, Pin>): Pin(name, pins) {
        var value: Int = 0
        override fun output(): Int {
            return value
        }
    }

    abstract class LogicGateOutput(name: String, val input1Name: String, val input2Name: String, pins: Map<String, Pin>): Pin(name, pins) {
        var value: Int? = null

        fun input1() = pins[input1Name]!!
        fun input2() = pins[input2Name]!!

        fun pinsInvolved(): Set<Pin> {
            val pin1 = pins[input1Name]!!
            val pin2 = pins[input2Name]!!
            return setOf(pin1, pin2) +
                    (if (pin1 is LogicGateOutput) pin1.pinsInvolved() else emptySet()) +
                            if (pin2 is LogicGateOutput) pin2.pinsInvolved() else emptySet()
        }

        abstract fun copy(name: String): LogicGateOutput
    }

    class OrGate(name: String, input1Name: String, input2Name: String, pins: Map<String, Pin>): LogicGateOutput(name, input1Name, input2Name, pins) {
        override fun output(): Int {
            if (value == null) {
                value = input1().output() or input2().output()
            }

            return value as Int
        }

        override fun copy(name: String): LogicGateOutput {
            return OrGate(name, input1Name, input2Name, pins)
        }
    }

    class XorGate(name: String, input1Name: String, input2Name: String, pins: Map<String, Pin>): LogicGateOutput(name, input1Name, input2Name, pins) {
        override fun output(): Int {
            if (value == null) {
                value = input1().output() xor input2().output()
            }

            return value as Int
        }

        override fun copy(name: String): LogicGateOutput {
            return XorGate(name, input1Name, input2Name, pins)
        }
    }

    class AndGate(name: String, input1Name: String, input2Name: String, pins: Map<String, Pin>): LogicGateOutput(name, input1Name, input2Name, pins) {
        override fun output(): Int {
            if (value == null) {
                value = input1().output() and input2().output()
            }

            return value as Int
        }

        override fun copy(name: String): LogicGateOutput {
            return AndGate(name, input1Name, input2Name, pins)
        }
    }

    fun calculatePartOne(input: List<String>): Long {
        val pins = mutableMapOf<String,Pin>()
        input.takeWhile { it.isNotEmpty() }.forEach {
            Regex("([xy])(\\d+): ([01])").matchEntire(it)?.destructured?.let { (xOrY, pin, value) ->
                pins["${xOrY}${pin}"] = ConstantPin("${xOrY}${pin}", value.toInt(), pins)
            }
        }
        input.dropWhile { it.isNotEmpty() }.drop(1).forEach {
            Regex("(.*) (OR|AND|XOR) (.*) -> (.*)").matchEntire(it)?.destructured?.let { (i1, op, i2, out) ->
                pins[out] = when (op) {
                    "OR" -> OrGate(out, i1, i2, pins)
                    "XOR" -> XorGate(out, i1, i2, pins)
                    "AND" -> AndGate(out, i1, i2, pins)
                    else -> throw IllegalStateException("Unrecognized op: $op")
                }
            }
        }

        var multiplier = 1L
        return pins.keys.filter { it.startsWith("z") }.sorted().map {
            val output = pins[it]!!.output()
            output
        }.fold(0L) { acc, i ->
            val result = acc + i * multiplier
            multiplier *= 2
            result
        }
    }

    fun calculatePartTwo(input: List<String>): String {
        val pins = mutableMapOf<String,Pin>()
        input.takeWhile { it.isNotEmpty() }.forEach {
            Regex("([xy])(\\d+): ([01])").matchEntire(it)?.destructured?.let { (xOrY, pin, _) ->
                pins["${xOrY}${pin}"] = VariablePin("${xOrY}${pin}", pins)
            }
        }
        input.dropWhile { it.isNotEmpty() }.drop(1).forEach {
            Regex("(.*) (OR|AND|XOR) (.*) -> (.*)").matchEntire(it)?.destructured?.let { (i1, op, i2, out) ->
                pins[out] = when (op) {
                    "OR" -> OrGate(out, i1, i2, pins)
                    "XOR" -> XorGate(out, i1, i2, pins)
                    "AND" -> AndGate(out, i1, i2, pins)
                    else -> throw IllegalStateException("Unrecognized op: $op")
                }
            }
        }

        val swapped = mutableListOf<String>()
        /*
        val xInputPins = pins.values.filterIsInstance<VariablePin>().filter { it.name.startsWith("x") }
        for (x in xInputPins) {
            val y = pins["y${x.name.drop(1)}"]!! as VariablePin
            pins.reset()
            x.value = 1
            y.value = 1
            val xValue = (1 shl x.name.drop(1).toInt()).toLong()
            val yValue = (1 shl y.name.drop(1).toInt()).toLong()
            val z1 = pins["z${x.name.drop(1)}"]
            val z2 = pins["z${(x.name.drop(1).toInt() + 1)}"] ?: continue
            if (calculateAddition(xValue, yValue, pins) != xValue + yValue) {
                val suspectPins = mutableSetOf<LogicGateOutput>()
                suspectPins += (z1 as LogicGateOutput).pinsInvolved()
                    .filterIsInstance<LogicGateOutput>()
                suspectPins += (z2 as LogicGateOutput).pinsInvolved()
                    .filterIsInstance<LogicGateOutput>()
                val suspects = suspectPins.toList()

                outer@ for (i in suspects.indices) {
                    val suspect = suspects[i]
                    for (toSwap in pins.entries) {
                        if (toSwap.value !is LogicGateOutput || suspect == toSwap.value ) continue
                        val other = toSwap.value as LogicGateOutput
                        pins[suspect.name] = other.copy(suspect.name)
                        pins[other.name] = suspect.copy(other.name)

                        if (validateCalculationsUntil(xValue, pins)) {
                            swapped.add(suspect.name)
                            swapped.add(other.name)
                            break@outer
                        } else {
                            pins[suspect.name] = suspect
                            pins[other.name] = other
                        }
                    }
                }
            }
        }


         */
        // find all output pins that aren't the result of an XOR
        swapped.addAll(pins.filter { it.key != "z45" && it.key.startsWith("z") }.values.filter { it is OrGate || it is AndGate }.map { it.name })

        // find all XORs that don't have x and y inputs or output to z
        swapped.addAll(pins.filterValues { it is XorGate }.filter {
            !(((it.value as LogicGateOutput).input1Name.first() in listOf('x', 'y') && (it.value as LogicGateOutput).input2Name.first() in listOf('x', 'y')) ||
                    it.key.startsWith("z"))
        }.values.map { it.name })

        // find all ORs that aren't attached to the outputs of ANDs that aren't the LSB
        swapped.addAll(pins.filterValues { it is OrGate }.filterNot {
            val or = it.value as OrGate
            (or.input1() is AndGate && or.input2() is AndGate)
        }.values.map { it.name })

        return swapped.sorted().joinToString(",")
    }

    private fun validateCalculationsUntil(
        upper: Long,
        pins: MutableMap<String, Pin>
    ): Boolean {
        val xInputPins =
            pins.values.filterIsInstance<VariablePin>().filter { it.name.startsWith("x") }
        for (x in xInputPins) {
            val y = pins["y${x.name.drop(1)}"]!! as VariablePin
            pins.reset()
            x.value = 1
            y.value = 1
            val xValue = (1 shl x.name.drop(1).toInt()).toLong()
            if (xValue > upper) return true
            val yValue = (1 shl y.name.drop(1).toInt()).toLong()
            if (calculateAddition(xValue, yValue, pins) != xValue + yValue) {
                return false
            }
        }

        return true
    }

    private fun calculateAddition(x: Long, y: Long, pins: Map<String,Pin>): Long {
        pins.filter { it.key.startsWith("x") }.forEach { entry ->
            (entry.value as VariablePin).value = if (x or 1 shl entry.key.drop(1).toInt() != 0L) 1 else 0
        }
        pins.filter { it.key.startsWith("y") }.forEach { entry ->
            (entry.value as VariablePin).value = if (y or 1 shl entry.key.drop(1).toInt() != 0L) 1 else 0
        }
        var multiplier = 1L
        return pins.keys.filter { it.startsWith("z") }.sorted().map {
            val output = pins[it]!!.output()
            output
        }.fold(0L) { acc, i ->
            val result = acc + i * multiplier
            multiplier *= 2
            result
        }
    }
}

private fun <String, Pin> MutableMap<String, Pin>.reset() {
    values.filter { it is Day24.VariablePin }.forEach { (it as Day24.VariablePin).value = 0 }
}