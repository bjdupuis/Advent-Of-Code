package days.aoc2023

import days.Day

class Day19 : Day(2023, 19) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    internal data class Part(val map: Map<String,Int>)
    internal class Rule(val condition: (Part) -> Boolean)
    internal class Workflow(val name: String, private val rules: List<Rule>) {
        fun invoke(part: Part) {
            print("$name -> ")
            rules.takeWhile { rule -> rule.condition.invoke(part) }
        }
    }

    fun calculatePartOne(input: List<String>): Int {
        val accepted = mutableListOf<Part>()
        val workflows = mutableMapOf<String, Workflow>()

        input.takeWhile { it.isNotEmpty() }.forEach { line ->
            val rules = mutableListOf<Rule>()
            Regex("(\\w+)\\{(.*)\\}").matchEntire(line)?.destructured?.let { (name, ruleDescription) ->
                ruleDescription.split(",").forEach { step ->
                    if (step == "R") {
                        rules.add(Rule { _ ->
                            println("R")
                            false
                        })
                    } else if (step == "A") {
                        rules.add(Rule { part ->
                            accepted.add(part)
                            println("A")
                            false
                        })
                    } else if (step.contains(":")) {
                        Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(step)?.destructured?.let { (register, comparison, value, destination) ->
                            rules.add(Rule { part ->
                                val other = value.toInt()
                                if (comparison == ">" && (part.map[register]!! > other) || comparison == "<" && (part.map[register]!! < other)) {
                                    if (destination == "A") {
                                        accepted.add(part)
                                        println("A")
                                    } else if (destination == "R") {
                                        println("R")
                                    } else {
                                        workflows[destination]!!.invoke(part)
                                    }
                                    false
                                } else {
                                    true
                                }
                            })
                        }
                    } else {
                        // it's a direct transfer
                        rules.add(Rule { part ->
                            workflows[step]?.invoke(part)
                            false
                        })
                    }
                }
                workflows[name] = Workflow(name, rules)
            }
        }

        input.dropWhile { it.isNotEmpty() }.drop(1).forEach { line ->
            val map = mutableMapOf<String,Int>()
            line.drop(1).dropLast(1).split(",").forEach { registerAndValue ->
                val (register, value) = registerAndValue.split("=")
                map[register] = value.toInt()
            }

            println("Processing new one")
            workflows["in"]!!.invoke(Part(map))
        }

        return accepted.sumOf { it.map.values.sum() }
    }

    fun calculatePartTwo(input: List<String>): Long {
        val ruleMap = mutableMapOf<String, String>()
        input.takeWhile { it.isNotEmpty() }.forEach { line ->
            Regex("(\\w+)\\{(.*)\\}").matchEntire(line)?.destructured?.let { (name, ruleDescription) ->
                ruleMap[name] = ruleDescription
            }
        }

        fun mergeMap(first: Map<String, Set<Int>>, second: Map<String, Set<Int>>): Map<String, Set<Int>> {
            val result = mutableMapOf<String, Set<Int>>()
            if (first.isEmpty()) {
                return emptyMap()
            }
            first.keys.plus(second.keys).distinct().forEach { key ->
                val s1 = first[key] ?: emptySet()
                val s2 = second[key] ?: emptySet()
                result[key] = s1 + s2
            }
        }

        fun traverseRule(name: String, potentials: Map<String, Set<Int>>): Map<String, Set<Int>> {
            val steps = ruleMap[name]!!.split(",")
            val currentPotentials = potentials.toMutableMap()
            outer@for (step in steps) {
                if (step == "R") {
                    return emptyMap()
                } else if (step == "A") {
                    return currentPotentials
                } else if (step.contains(":")) {
                    Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(step)?.destructured?.let { (register, comparison, value, destination) ->
                        val other = value.toInt()
                        if (comparison == ">") {
                            val mySet = (currentPotentials[register] ?: (1..4000).toSet()).filter { it > other }.toSet()
                            if (destination == "A") {
                                currentPotentials[register] = mySet
                                break@outer;
                            } else if (destination == "R") {
                                return emptyMap()
                            } else {
                                val otherSet = (potentials[register] ?: (1..4000).toSet()).filter { it <= other }.toSet()
                                return mergeMap(traverseRule(destination, potentials.plus(register to mySet)),
                            }
                        } else {
                            if (destination == "A") {
                                return potentials.toMutableMap().apply { this[register]!!.filter { it < other } }
                            } else if (destination == "R") {
                                return emptyMap()
                            } else {
                                return mergeMap(traverseRule(destination, potentials.toMutableMap().apply { this[register]!!.filter { it < other } }), potentials)
                            }
                        }
                    }
                } else {
                    // it's a direct transfer
                    return mergeMap(traverseRule(step, potentials), potentials)
                }
            }
        }

        val result = traverseRule("in", mapOf<String,Set<Int>>())

        return result.values.fold(1L) { acc, list -> acc * list.count() }
    }
}