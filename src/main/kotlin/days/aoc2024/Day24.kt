package days.aoc2024

import days.Day
import kotlin.math.max
import kotlin.math.min

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

        override fun toString(): String {
            return name
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
        var alias: String? = null
            get() = field ?: name

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
        override fun toString(): String {
            return "$alias"
        }
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

        override fun toString(): String {
            return "$input1Name OR $input2Name -> $alias"
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

        override fun toString(): String {
            return "${input1()} XOR ${input2()} -> ${alias ?: name}"
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

        override fun toString(): String {
            return "$input1Name AND $input2Name -> ${alias ?: name}"
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

    class SimplePin(val name: String)

    class Gate(val in1: String, val in2: String, var output: String, val operation: String) {
        private var alias: String? = null
        fun aliasOf(gates: List<Gate>): String {
            if (alias != null) {
                return alias as String
            } else {
                val p1 = inputGate1(gates)
                val p2 = inputGate2(gates)

                alias = if (operation == "XOR" && in1.drop(1) == in2.drop(1)) {
                    "XOR${in1.drop(1)}"
                } else if (operation == "AND" && in1.drop(1) == in2.drop(1)) {
                    if (in1.drop(1) == "00") "CARRY00" else "AND${in1.drop(1)}"
                } else if (operation == "AND" &&
                    p2.matches(Regex("XOR${p2.dropWhile { !it.isDigit() }}")) &&
                    p1.matches(Regex("CARRY${(p2.dropWhile { !it.isDigit() }.toInt() - 1).toString().padStart(2, '0')}"))
                ) {
                    "CARRY_INTERMEDIATE${p2.dropWhile { !it.isDigit() }}"
                } else if (operation == "OR" &&
                    p1.matches(Regex("AND${p1.dropWhile { !it.isDigit() }}")) &&
                    p2.matches(Regex("CARRY_INTERMEDIATE${p1.dropWhile { !it.isDigit() }}"))
                ) {
                    "CARRY${p1.dropWhile { !it.isDigit() }}"
                } else {
                    output
                }
                return alias as String
            }
        }

        fun print(gates: List<Gate>) {
            val p1 = inputGate1(gates)
            val p2 = inputGate2(gates)

            println("$p1 $operation $p2 -> ${aliasOf(gates)}")
        }

        fun inputGate1(gates: List<Gate>): String {
            val p1 = if (in1.startsWith("x") || in1.startsWith("y"))
                in1
            else
                gates.first { it.output == in1 }.aliasOf(gates)
            val p2 = if (in2.startsWith("x") || in2.startsWith("y"))
                in2
            else
                gates.first { it.output == in2 }.aliasOf(gates)
            return if (p1 < p2) p1 else p2
        }

        fun inputGate2(gates: List<Gate>): String {
            val p1 = if (in1.startsWith("x") || in1.startsWith("y"))
                in1
            else
                gates.first { it.output == in1 }.aliasOf(gates)
            val p2 = if (in2.startsWith("x") || in2.startsWith("y"))
                in2
            else
                gates.first { it.output == in2 }.aliasOf(gates)
            return if (p1 > p2) p1 else p2
        }
    }

    fun calculatePartTwo(input: List<String>): String {
        val pins = mutableListOf<String>()
        input.takeWhile { it.isNotEmpty() }.forEach {
            Regex("([xy])(\\d+): ([01])").matchEntire(it)?.destructured?.let { (xOrY, pin, _) ->
                pins.add("${xOrY}${pin}")
            }
        }
        val gates = mutableListOf<Gate>()

        // yes, these are found by manual inspection looking for incorrect patterns for a 2-bit adder with carry 😬
        val swaps = listOf(
            listOf("ksv", "z06"),
            listOf("kbs", "nbd"),
            listOf("tqq", "z20"),
            listOf("ckb", "z39")
        )
        input.dropWhile { it.isNotEmpty() }.drop(1).forEach {
            Regex("(.*) (OR|AND|XOR) (.*) -> (.*)").matchEntire(it)?.destructured?.let { (i1, op, i2, out) ->
                var output = out
                for (swap in swaps) {
                    if (swap.contains(out)) {
                        output = swap.first { it != out }
                        break
                    }
                }
                gates.add(Gate(i1, i2, output, op))
            }
        }

        val toBePrinted = mutableSetOf<Gate>()
        toBePrinted.addAll(gates)
        for (i in 0..44) {
            for (gate in gates) {
                if (toBePrinted.contains(gate)) {
                    val gate1 = gate.inputGate1(gates)
                    val gate1Number = if (gate1.matches(Regex(".*\\d\\d"))) gate1.dropWhile { !it.isDigit() }.toInt() else Int.MIN_VALUE
                    val gate2 = gate.inputGate2(gates)
                    val gate2Number = if (gate2.matches(Regex(".*\\d\\d"))) gate2.dropWhile { !it.isDigit() }.toInt() else Int.MIN_VALUE
                    val max = max(gate1Number, gate2Number)
                    val gateNumber = if (gate.aliasOf(gates).matches(Regex(".*\\d\\d"))) gate.aliasOf(gates).dropWhile { !it.isDigit() }.toInt() else Int.MAX_VALUE
                    if (max == i || gateNumber == i) {
                        gate.print(gates)
                        toBePrinted.remove(gate)
                    }
                }
            }
            println("")
        }

        println("=== not printed ===")
        for(gate in toBePrinted) {
            gate.print(gates)
        }

        val swapped = mutableListOf<String>()
        swapped.addAll(swaps.flatten())
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


         */
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