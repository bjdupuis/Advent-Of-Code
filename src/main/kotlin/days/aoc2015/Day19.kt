package days.aoc2015

import days.Day

class Day19: Day(2015, 19) {
    override fun partOne(): Any {
        val replacements = inputList.takeWhile { it.isNotBlank() }
        val target = inputList.last()

        return calculatePartOne(replacements, target)
    }

    override fun partTwo(): Any {
        val replacements = inputList.takeWhile { it.isNotBlank() }
        val target = inputList.last()

        return calculatePartTwo(replacements, target)
    }

    fun calculatePartOne(replacements: List<String>, molecule: String): Int {
        val distinctMolecules = mutableSetOf<String>()

        replacements.forEach { replacement ->
            val (searchFor, replaceWith) = replacement.trim().split(" => ")
            Regex(searchFor).findAll(molecule).forEach {
                val newOne = StringBuilder(molecule.substring(0, it.range.first))
                newOne.append(replaceWith)
                newOne.append(molecule.substring(it.range.first + searchFor.length))

                println("$searchFor => $replaceWith:  $newOne")
                distinctMolecules.add(newOne.toString())
            }
        }

        return distinctMolecules.size
    }

    fun calculatePartTwo(replacements: List<String>, molecule: String): Int {
        val graph = Graph()
        replacements.forEach { replacement ->
            Regex("(\\w+) => (\\w+)").matchEntire(replacement)?.destructured?.let { (name, replacement) ->
                graph.addNode(name, replacement)
            }
        }

        findAllPaths(graph.nodeFor("e"), molecule).minByOrNull { it.size }?.let {
            return it.size
        }

        return 0
    }

    private fun findAllPaths(
        current: Graph.Node?,
        remainingMolecule: String
    ): List<List<Graph.Node>> {
        // yeah... maybe not. 😭
        return emptyList()
    }

    class Graph {
        private val nodeMap = mutableMapOf<String,Node>()

        fun nodeFor(name: String): Node? {
            return nodeMap[name]
        }

        fun addNode(name: String, replacement: String) {
            nodeMap.getOrPut(name) { Node(name) }.replacements.add(replacement)
        }

        class Node(val name: String) {
            val replacements = mutableListOf<String>()
        }
    }
}