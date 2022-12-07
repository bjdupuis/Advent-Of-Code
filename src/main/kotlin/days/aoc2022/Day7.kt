package days.aoc2022

import days.Day

class Day7 : Day(2022, 7) {
    override fun partOne(): Any {
        return calculateSumOfDirectoriesUnder100000(inputList)
    }

    override fun partTwo(): Any {
        return calculateSmallestDirectoryThatCanBeDeleted(inputList)
    }

    fun calculateSmallestDirectoryThatCanBeDeleted(input: List<String>): Int {
        val root = buildHierarchy(input)

        val neededSpace = 30000000 - (70000000 - root.size())
        return childrenSequence(root).map { it.size() }.filter { it > neededSpace }.min()
    }

    fun calculateSumOfDirectoriesUnder100000(input: List<String>): Int {
        val root = buildHierarchy(input)

        return childrenSequence(root).filter { it.size() <= 100000 }.sumOf { it.size() }
    }

    private fun buildHierarchy(input: List<String>): Directory {
        val root = Directory("/", null)
        var current = root
        input.forEach { line ->
            when {
                line.startsWith("${'$'} cd") -> {
                    val directoryToMoveTo = line.split(' ').last()!!
                    current = when (directoryToMoveTo) {
                        ".." -> {
                            current.parent!!
                        }
                        "/" -> {
                            root
                        }
                        else -> {
                            current.children.first { it.name == directoryToMoveTo }
                        }
                    }
                }
                line.startsWith("${'$'} ls") -> {
                    // noop
                }
                line.startsWith("dir ") -> {
                    current.children.add(Directory(line.split(" ").last(), current))
                }
                else -> {
                    val parts = line.split(" ")
                    current.files.add(File(parts.last(), parts.first().toInt()))
                }
            }
        }

        return root
    }

    class Directory(val name: String, val parent: Directory?) {
        val children = mutableListOf<Directory>()
        val files = mutableListOf<File>()

        fun size(): Int {
            return files.sumOf { it.size } + children.sumOf { it.size() }
        }
    }

    private fun childrenSequence(start: Directory): Sequence<Directory> = sequence {
        start.children.forEach { child ->
            yield(child)
            yieldAll(childrenSequence(child))
        }
    }

    data class File(val name: String, val size: Int)
}