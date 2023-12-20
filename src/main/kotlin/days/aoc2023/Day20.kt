package days.aoc2023

import days.Day

class Day20 : Day(2023, 20) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    enum class Pulse {
        LOW, HIGH
    }

    abstract class Module(val name: String) {
        protected val destinations = mutableListOf<Module>()
        abstract fun processSignal(from: Module, pulse: Pulse): List<Pair<Pulse, Module>>

        fun addDestination(destination: Module) { destinations.add(destination) }

        open fun addSource(source: Module) {}
    }

    class Broadcaster(name: String): Module(name) {
        override fun processSignal(from: Module, pulse: Pulse): List<Pair<Pulse, Module>> {
            return destinations.map { pulse to it }
        }
    }

    class DeadEnd(name: String): Module(name) {
        override fun processSignal(from: Module, pulse: Pulse): List<Pair<Pulse, Module>> {
            return emptyList()
        }
    }

    class FlipFlop(name: String): Module(name) {
        private var poweredUp = false

        override fun processSignal(from: Module, pulse: Pulse): List<Pair<Pulse, Module>> {
            return if (pulse == Pulse.LOW) {
                val wasPoweredUp = poweredUp
                poweredUp = !poweredUp
                if (wasPoweredUp) {
                    destinations.map { Pulse.LOW to it }
                } else {
                    destinations.map { Pulse.HIGH to it }
                }
            } else {
                emptyList()
            }
        }
    }

    class Conjunction(name: String): Module(name) {
        private val memory = mutableMapOf<Module, Pulse>()

        override fun addSource(source: Module) {
            super.addSource(source)
            if (!memory.containsKey(source)) {
                memory[source] = Pulse.LOW
            }
        }

        override fun processSignal(from: Module, pulse: Pulse): List<Pair<Pulse, Module>> {
            memory[from] = pulse
            return if (memory.all { it.value == Pulse.HIGH } ) {
                destinations.map { Pulse.LOW to it }
            } else {
                destinations.map { Pulse.HIGH to it }
            }
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val BROADCASTER = "broadcaster"
        val modules = mutableMapOf<String, Module>()

        // step through and create all the modules first
        input.forEach { moduleDescriptor ->
            if (moduleDescriptor.startsWith("%")) {
                val name = moduleDescriptor.drop(1).takeWhile { it != ' ' }
                modules[name] = FlipFlop(name)
            } else if (moduleDescriptor.startsWith("&")) {
                val name = moduleDescriptor.drop(1).takeWhile { it != ' ' }
                modules[name] = Conjunction(name)
            } else if (moduleDescriptor.startsWith(BROADCASTER)) {
                modules[BROADCASTER] = Broadcaster(BROADCASTER)
            }
        }

        // now hook up the destinations
        input.forEach { moduleDescriptor ->
            val name = if (moduleDescriptor.startsWith(BROADCASTER)) BROADCASTER else moduleDescriptor.drop(1).takeWhile { it != ' ' }
            moduleDescriptor.dropWhile { it != '>' }.drop(2).split(",").forEach { destinationName ->
                val destination = modules.getOrPut(destinationName.trim()) { DeadEnd(destinationName.trim()) }
                modules[name]!!.addDestination(destination)
                destination.addSource(modules[name]!!)
            }
        }

        val broadcaster = modules[BROADCASTER]!!
        var lowPulseCount = 0
        var highPulseCount = 0

        class PulseTransmission(val from: Module, val to: Module, val pulse: Pulse)

        val pulseQueue = mutableListOf<PulseTransmission>()
        repeat(1000) {
            lowPulseCount++
            pulseQueue.add(PulseTransmission(broadcaster, broadcaster, Pulse.LOW))

            while (pulseQueue.isNotEmpty()) {
                val pulseToDeliver = pulseQueue.removeFirst()
                val pulsesToDeliver = pulseToDeliver.to.processSignal(pulseToDeliver.from, pulseToDeliver.pulse)
                highPulseCount += pulsesToDeliver.count { it.first == Pulse.HIGH }
                lowPulseCount += pulsesToDeliver.count { it.first == Pulse.LOW }

                pulsesToDeliver.forEach {
                    pulseQueue.add(PulseTransmission(pulseToDeliver.to, it.second, it.first))
                }
            }
        }

        return lowPulseCount * highPulseCount
    }

    fun calculatePartTwo(input: List<String>): Long {
        val BROADCASTER = "broadcaster"
        val modules = mutableMapOf<String, Module>()

        // step through and create all the modules first
        input.forEach { moduleDescriptor ->
            if (moduleDescriptor.startsWith("%")) {
                val name = moduleDescriptor.drop(1).takeWhile { it != ' ' }
                modules[name] = FlipFlop(name)
            } else if (moduleDescriptor.startsWith("&")) {
                val name = moduleDescriptor.drop(1).takeWhile { it != ' ' }
                modules[name] = Conjunction(name)
            } else if (moduleDescriptor.startsWith(BROADCASTER)) {
                modules[BROADCASTER] = Broadcaster(BROADCASTER)
            }
        }

        // now hook up the destinations
        input.forEach { moduleDescriptor ->
            val name = if (moduleDescriptor.startsWith(BROADCASTER)) BROADCASTER else moduleDescriptor.drop(1).takeWhile { it != ' ' }
            moduleDescriptor.dropWhile { it != '>' }.drop(2).split(",").forEach { destinationName ->
                val destination = modules.getOrPut(destinationName.trim()) { DeadEnd(destinationName.trim()) }
                modules[name]!!.addDestination(destination)
                destination.addSource(modules[name]!!)
            }
        }

        val broadcaster = modules[BROADCASTER]!!

        class PulseTransmission(val from: Module, val to: Module, val pulse: Pulse)

        val pulseQueue = mutableListOf<PulseTransmission>()
        var buttonPressedCount = 0L
        while(true) {
            buttonPressedCount++
            pulseQueue.add(PulseTransmission(broadcaster, broadcaster, Pulse.LOW))

            while (pulseQueue.isNotEmpty()) {
                val pulseToDeliver = pulseQueue.removeFirst()
                val pulsesToDeliver = pulseToDeliver.to.processSignal(pulseToDeliver.from, pulseToDeliver.pulse)
                if (pulsesToDeliver.any { it.first == Pulse.LOW && it.second.name == "rx" }) {
                    return buttonPressedCount
                }
                pulsesToDeliver.forEach {
                    pulseQueue.add(PulseTransmission(pulseToDeliver.to, it.second, it.first))
                }
            }
        }
    }
}