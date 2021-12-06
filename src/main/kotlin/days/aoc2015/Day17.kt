package days.aoc2015

import days.Day

class Day17: Day(2015, 17) {
    override fun partOne(): Any {
        return calculatePartOne(150)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(150)
    }

    fun calculatePartTwo(target: Int): Int {
        val containers = inputList.map { it.toInt() }.sorted()

        return createSolutions(target, containers, null, mutableListOf()).let { solutions ->
            solutions.map { it.size }.let { solutionSizes ->
                val minContainerCount = solutionSizes.minOrNull()!!
                solutionSizes.filter { it == minContainerCount }
            }.count()
        }
    }

    fun calculatePartOne(target: Int): Int {
        val containers = inputList.map { it.toInt() }.sorted()

        return countSolutionsForRemainingContainers(target, containers)
    }

    fun createSolutions(remainingEggnog: Int, remainingContainers: List<Int>, potentialSolution: List<Int>?, currentSolutions: List<List<Int>>): List<List<Int>> {
        val solution = potentialSolution ?: mutableListOf()
        return if (remainingContainers.size == 1) {
            if (remainingEggnog == remainingContainers[0]) currentSolutions.plusElement(solution.plus(remainingEggnog) ) else currentSolutions
        } else {
            val largest = remainingContainers.last()
            val additionalSolutions = createSolutions(remainingEggnog, remainingContainers.dropLast(1), solution, currentSolutions)

            when {
                remainingEggnog == largest -> {
                    return currentSolutions.plus(additionalSolutions).plusElement(solution.plus(largest))
                }
                remainingEggnog < largest -> {
                    return currentSolutions.plus(additionalSolutions)
                }
                else -> {
                    return currentSolutions.plus(createSolutions(remainingEggnog - largest, remainingContainers.dropLast(1), solution.plus(largest), currentSolutions)).plus(additionalSolutions)
                }
            }
        }
    }

    fun countSolutionsForRemainingContainers(remainingEggnog: Int, remainingContainers: List<Int>): Int {
        return if (remainingContainers.size == 1) {
            if (remainingEggnog == remainingContainers[0]) 1 else 0
        } else {
            val largest = remainingContainers.last()
            val otherSolutions = countSolutionsForRemainingContainers(remainingEggnog, remainingContainers.dropLast(1))

            when {
                remainingEggnog == largest -> {
                    1 + otherSolutions
                }
                remainingEggnog < largest -> {
                    otherSolutions
                }
                else -> {
                    otherSolutions + countSolutionsForRemainingContainers(remainingEggnog - largest, remainingContainers.dropLast(1))
                }
            }
        }
    }

}

