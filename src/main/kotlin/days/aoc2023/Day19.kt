package days.aoc2023

import days.Day
import java.lang.IllegalStateException

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
                            false
                        })
                    } else if (step == "A") {
                        rules.add(Rule { part ->
                            accepted.add(part)
                            false
                        })
                    } else if (step.contains(":")) {
                        Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(step)?.destructured?.let { (register, comparison, value, destination) ->
                            rules.add(Rule { part ->
                                val other = value.toInt()
                                if (comparison == ">" && (part.map[register]!! > other) || comparison == "<" && (part.map[register]!! < other)) {
                                    if (destination == "A") {
                                        accepted.add(part)
                                    } else if (destination == "R") {
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
            first.keys.plus(second.keys).distinct().forEach { key ->
                val s1 = first[key] ?: emptySet()
                val s2 = second[key] ?: emptySet()
                result[key] = s1 + s2
            }
            return result
        }

        class Dispositions(val accepted: Map<String, Set<Int>>, val rejected: Map<String, Set<Int>>)

        fun traverseRule(name: String, current: Map<String, Set<Int>>, dispositions: Dispositions): Dispositions {
            var steps = ruleMap[name]!!.split(",").toMutableList()

            print("Rule $name - ")

            fun processSteps(steps: List<String>, current: Map<String, Set<Int>>, dispositions: Dispositions): Dispositions {
                val step = steps.first()
                if (step == "R") {
                    return Dispositions(dispositions.accepted, mergeMap(dispositions.rejected, current))
                } else if (step == "A") {
                    return Dispositions(mergeMap(dispositions.accepted, current), dispositions.rejected)
                } else if (step.contains(":")) {
                    Regex("([xmas])([<>])(\\d+):(\\w+)").matchEntire(step)?.destructured?.let { (register, comparison, valueString, destination) ->
                        val value = valueString.toInt()
                        if (comparison == ">") {
                            val mySet = (current[register] ?: (1..4000).toSet()).filter { it > value }.toSet()
                            val otherSet = (current[register] ?: (1..4000).toSet()).filter { it <= value }.toSet()

                            if (destination == "A") {
                                return processSteps(steps.drop(1), current.plus(register to otherSet), Dispositions(mergeMap(dispositions.accepted, current.plus(register to mySet)), dispositions.rejected))
                            } else if (destination == "R") {
                                return processSteps(steps.drop(1), current.plus(register to otherSet), Dispositions(dispositions.accepted, mergeMap(dispositions.rejected, current.plus(register to mySet))))
                            } else {
                                val positiveResult = traverseRule(destination, current.plus(register to mySet), dispositions)
                                val negativeResult = processSteps(steps.drop(1), current.plus(register to otherSet), dispositions)
                                return Dispositions(mergeMap(positiveResult.accepted, negativeResult.accepted), mergeMap(positiveResult.rejected, negativeResult.rejected))
                            }
                        } else {
                            val mySet = (current[register] ?: (1..4000).toSet()).filter { it < value }.toSet()
                            val otherSet = (current[register] ?: (1..4000).toSet()).filter { it >= value }.toSet()

                            if (destination == "A") {
                                return processSteps(steps.drop(1), current.plus(register to otherSet), Dispositions(mergeMap(dispositions.accepted, current.plus(register to mySet)), dispositions.rejected))
                            } else if (destination == "R") {
                                return processSteps(steps.drop(1), current.plus(register to otherSet), Dispositions(dispositions.accepted, mergeMap(dispositions.rejected, current.plus(register to mySet))))
                            } else {
                                val positiveResult = traverseRule(destination, current.plus(register to mySet), dispositions)
                                val negativeResult = processSteps(steps.drop(1), current.plus(register to otherSet), dispositions)
                                return Dispositions(mergeMap(positiveResult.accepted, negativeResult.accepted), mergeMap(positiveResult.rejected, negativeResult.rejected))
                            }
                        }
                    } ?: throw IllegalStateException("how are we here?")
                } else {
                    // it's a direct transfer
                    return traverseRule(step, current, dispositions)
                }
            }

            return processSteps(steps, current, dispositions)
        }

        val result = traverseRule("in", emptyMap(), Dispositions(emptyMap(), emptyMap()))

        val final = result.accepted.map { entry -> entry.value.filter { !result.rejected[entry.key]!!.contains(it) } }

        return final.fold(1L) { acc, list -> acc * list.count() }
    }
}