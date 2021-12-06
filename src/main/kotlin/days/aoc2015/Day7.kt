package days.aoc2015

import days.Day

class Day7: Day(2015, 7) {
    val circuit = Circuit()

    init {
        circuit.createCircuit(inputList)
    }

    override fun partOne(): Any {
        return circuit.getGate("a").output!!
    }

    override fun partTwo(): Any {
        circuit.createCircuit(inputList)
        circuit.getGate("b").output = 3176u
        return circuit.getGate("a").output!!
    }

    class Circuit {
        private val gates: MutableMap<String, Gate> = mutableMapOf()

        fun getGate(name: String): Gate {
            return gates[name]
                    ?: error("Unable to get gate $name")
        }

        fun createCircuit(inputList: List<String>) {
            gates.clear()
            inputList.forEach {
                when {
                    it.contains("NOT") -> {
                        Regex("NOT (\\w+) -> (\\w+)").matchEntire(it)?.destructured?.let { (first, name) ->
                            gates[name] = NotGate(first)
                        }

                    }
                    it.contains("AND") -> {
                        Regex("(\\w+) AND (\\w+) -> (\\w+)").matchEntire(it)?.destructured?.let { (first, second, name) ->
                            gates[name] = AndGate(first, second)
                        }
                    }
                    it.contains("OR") -> {
                        Regex("(\\w+) OR (\\w+) -> (\\w+)").matchEntire(it)?.destructured?.let { (first, second, name) ->
                            gates[name] = OrGate(first, second)
                        }
                    }
                    it.contains("LSHIFT") -> {
                        Regex("(\\w+) LSHIFT (\\d+) -> (\\w+)").matchEntire(it)?.destructured?.let { (first, second, name) ->
                            gates[name] = LshiftGate(first, second)
                        }
                    }
                    it.contains("RSHIFT") -> {
                        Regex("(\\w+) RSHIFT (\\d+) -> (\\w+)").matchEntire(it)?.destructured?.let { (first, second, name) ->
                            gates[name] = RshiftGate(first, second)
                        }
                    }
                    else -> {
                        Regex("(.+) -> (\\w+)").matchEntire(it)?.destructured?.let { (signal, name) ->
                            gates[name] = if (signal.contains(Regex("\\d"))) {
                                StaticGate(signal.toUShort())
                            } else {
                                ReferenceGate(signal)
                            }
                        }
                    }
                }
            }
        }

        abstract inner class Gate {
            var output: UShort? = null
            get() {
                if (field == null) {
                    field = calculateOutput()
                }
                return field
            }
            abstract fun calculateOutput(): UShort
        }

        inner class StaticGate(private val signal: UShort): Gate() {
            override fun calculateOutput(): UShort {
                return signal
            }
        }

        inner class ReferenceGate(private val wire: String): Gate() {
            override fun calculateOutput(): UShort {
                return getGate(wire).output!!
            }
        }

        inner class NotGate(private val first: String): Gate() {
            override fun calculateOutput(): UShort {
                return getGate(first).output!!.inv()
            }
        }


        abstract inner class InfixGate(private val first: String, private val second: String, private val operator: (UShort, UShort) -> UShort): Gate() {
            override fun calculateOutput(): UShort {
                return operator(if (first.contains(Regex("\\d"))) {
                    first.toUShort()
                } else {
                    getGate(first).output!!
                }, if (second.contains(Regex("\\d"))) {
                    second.toUShort()
                } else {
                    getGate(second).output!!
                })
            }
        }

        inner class AndGate(private val first: String, private val second: String): InfixGate(first, second, UShort::and)
        inner class OrGate(private val first: String, private val second: String): InfixGate(first, second, UShort::or)

        inner class LshiftGate(private val first: String, private val second: String): Gate() {

            override fun calculateOutput(): UShort {
                return (getGate(first).output!!.toInt() shl second.toInt()).toUShort()
            }
        }

        inner class RshiftGate(private val first: String, private val second: String): Gate() {
            override fun calculateOutput(): UShort {
                return (getGate(first).output!!.toInt() shr second.toInt()).toUShort()
            }
        }

    }
}