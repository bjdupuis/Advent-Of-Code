package days.aoc2015

import days.Day

class Day16: Day(2015, 16) {
    override fun partOne(): Any {
        return inputList.first { matchesCorrectSue(it) }
    }

    fun matchesCorrectSue(sueDescription: String): Boolean {
        val propertyMap = mutableMapOf<String,Int>()
        val matchingSue =
"""
children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1
"""
        .trimIndent().lines().map {
            Regex("(\\w+): (\\d+)").matchEntire(it)?.destructured?.let { (property,amount) ->
                propertyMap[property] = amount.toInt()
            }
        }

        return Regex("Sue (\\d+): (.*)").matchEntire(sueDescription)!!.destructured?.let { (sueNumber,properties) ->
            properties.split(", ").all {
                Regex("(\\w+): (\\d+)").matchEntire(it)?.destructured?.let { (propertyName,propertyAmount) ->
                    propertyMap[propertyName]!! == propertyAmount.toInt()
                } ?: false
            }
        }
    }

    fun matchesRealCorrectSue(sueDescription: String): Boolean {
        val propertyMap = mutableMapOf<String,Int>()
        val matchingSue =
                """
children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1
"""
                        .trimIndent().lines().map {
                            Regex("(\\w+): (\\d+)").matchEntire(it)?.destructured?.let { (property,amount) ->
                                propertyMap[property] = amount.toInt()
                            }
                        }

        return Regex("Sue (\\d+): (.*)").matchEntire(sueDescription)!!.destructured?.let { (sueNumber,properties) ->
            properties.split(", ").all {
                Regex("(\\w+): (\\d+)").matchEntire(it)?.destructured?.let { (propertyName,propertyAmount) ->
                    when (propertyName) {
                        "cats", "trees" -> {
                            propertyAmount.toInt() > propertyMap[propertyName]!!
                        }
                        "pomeranians", "goldfish" -> {
                            propertyAmount.toInt() < propertyMap[propertyName]!!
                        }
                        else -> {
                            propertyAmount.toInt() == propertyMap[propertyName]!!
                        }
                    }
                } ?: false
            }
        }
    }

    override fun partTwo(): Any {
        return inputList.first { matchesRealCorrectSue(it) }
    }
}