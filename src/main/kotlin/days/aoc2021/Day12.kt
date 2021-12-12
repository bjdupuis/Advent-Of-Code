package days.aoc2021

import days.Day

class Day12 : Day(2021, 12) {
    override fun partOne(): Any {
        return calculateNumberOfPaths(inputList)
    }

    override fun partTwo(): Any {
        return calculateNumberOfPathsVisitSmallCavesTwice(inputList)
    }

    fun calculateNumberOfPaths(inputList: List<String>): Int {
        val graph = Graph()
        inputList.forEach { line ->
            Regex("(\\w+)-(\\w+)").matchEntire(line.trim())?.destructured?.let { (node1, node2) ->
                graph.addNodesAndEdges(node1, node2)
            }
        }

        return graph.countPathsToEndVisitedSmallNodesOnce()
    }

    fun calculateNumberOfPathsVisitSmallCavesTwice(inputList: List<String>): Int {
        val graph = Graph()
        inputList.forEach { line ->
            Regex("(\\w+)-(\\w+)").matchEntire(line.trim())?.destructured?.let { (node1, node2) ->
                graph.addNodesAndEdges(node1, node2)
            }
        }

        return graph.countPathsToEndVisitedSmallNodesTwice()
    }


    class Graph {

        private var nodes = mutableMapOf<String, Node>()

        fun addNodesAndEdges(first: String, second: String) {
            if (!nodes.contains(first)) {
                nodes[first] = Node(first)
            }
            if (!nodes.contains(second)) {
                nodes[second] = Node(second)
            }
            nodes[first]!!.edgesTo.add(nodes[second]!!)
            nodes[second]!!.edgesTo.add(nodes[first]!!)
        }

        fun countPathsToEndVisitedSmallNodesOnce(): Int {
            return recursePathsToEnd(nodes["start"], nodes["end"], listOf(), mutableListOf()).size
        }

        fun countPathsToEndVisitedSmallNodesTwice(): Int {
            return recursePathsToEnd(nodes["start"], nodes["end"], listOf(), mutableListOf(), true).size
        }

        private fun recursePathsToEnd(
            current: Node?,
            end: Node?,
            currentPath: List<Node>,
            foundPaths: MutableList<List<Node>>,
            visitASmallCaveTwice: Boolean = false,
        ): List<List<Node>> {
            current?.edgesTo?.forEach { next ->
                if (next == end) {
                    foundPaths.add(currentPath.plus(next))
                } else {
                    val caveCount = currentPath.count { it == next }
                    if (caveCount == 0 || next.name.first().isUpperCase()) {
                        recursePathsToEnd(next, end, currentPath.plusElement(current), foundPaths, visitASmallCaveTwice)
                    } else if (next.name != "start" && caveCount == 1 && visitASmallCaveTwice) {
                        recursePathsToEnd(next, end, currentPath.plusElement(current), foundPaths, false)
                    }
                }
            }

            return foundPaths
        }

        private fun dumpCurrentPath(currentPath: List<Node>) {
            currentPath.forEach {
                print("${it.name} ")
            }
            println()
        }

        class Node(val name: String) {
            val edgesTo = mutableListOf<Node>()
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Node

                if (name != other.name) return false

                return true
            }

            override fun hashCode(): Int {
                return name.hashCode()
            }
        }
    }
}