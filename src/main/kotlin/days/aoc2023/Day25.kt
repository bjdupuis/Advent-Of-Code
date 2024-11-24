package days.aoc2023

import days.Day

class Day25 : Day(2023, 25) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    data class Component(val name: String) {
        val connections = mutableListOf<Component>()

        fun disconnectFrom(connection: Component) {
            connections.remove(connection)
            connection.connections.remove(this)
        }

        fun isConnectedTo(component: Day25.Component): Boolean {
            val queue = ArrayDeque<Component>()
            val seen = mutableListOf<Component>()
            seen.add(this)
            queue.addAll(connections)
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                seen.add(current)
                if (current == component) {
                    return true
                }
                queue.addAll(current.connections.filter { !seen.contains(it) })
            }
            return false
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val components = mutableMapOf<String, Component>()
        input.forEach { line ->
            line.split(":").let {
                val component = components.getOrPut(it.first().trim()) { Component(it.first().trim()) }
                it.last().trim().split(" ").map { connectedTo ->
                    val other = components.getOrPut(connectedTo.trim()) { Component(connectedTo.trim()) }
                    component.connections.add(other)
                    other.connections.add(component)
                }
            }
        }

        // dump graphviz 😬
        // remove the 3 important ones: gzr to qnz, hgk to pgz, lmj to xgs
        components["gzr"]!!.disconnectFrom(components["qnz"]!!)
        components["hgk"]!!.disconnectFrom(components["pgz"]!!)
        components["lmj"]!!.disconnectFrom(components["xgs"]!!)

        val group1 = components.values.filter { it.isConnectedTo(components["gzr"]!!) }
        val group2 = components.values.filter { it.isConnectedTo(components["qnz"]!!) }

        return (group1.size + 1) * (group2.size + 1)
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}