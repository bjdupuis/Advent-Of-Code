package days.aoc2020

import days.Day

class Day6: Day(2020, 6) {
    override fun partOne(): Any {
        var currentLine = ""
        val customsFormsList: MutableList<String> = mutableListOf()
        inputList.forEach {
            if (it.isBlank()) {
                customsFormsList.add(currentLine)
                currentLine = ""
            } else {
                currentLine += it
            }
        }
        customsFormsList.add(currentLine)

        return customsFormsList.map { it.chars().distinct().count() }.sum().toInt()
    }

    override fun partTwo(): Any {
        var questionsAnsweredByAll = 0
        var currentlyAnswered = "*"

        inputList.forEach { currentCustomsForm ->
            if (currentCustomsForm.isBlank()) {
                questionsAnsweredByAll += currentlyAnswered.length
                currentlyAnswered = "*"
            } else {
                currentlyAnswered = if (currentlyAnswered == "*") {
                    currentCustomsForm
                } else {
                    currentlyAnswered.filter {
                        currentCustomsForm.contains(it)
                    }
                }
            }
        }

        if (currentlyAnswered != "*") {
            questionsAnsweredByAll += currentlyAnswered.length
        }

        return questionsAnsweredByAll
    }
}