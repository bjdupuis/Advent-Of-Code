package days.aoc2020

import days.Day

class Day19 : Day(2020, 19) {
    override fun partOne(): Any {
        val ruleMap = createRuleMap(inputList)
        val regexString = generateRegex(ruleMap["0"] ?: error("no rule 0"), ruleMap)
        val regex = Regex(regexString)

        return inputList.subList(inputList.indexOfFirst { it.isBlank() } + 1, inputList.size).map {
            regex.matches(it)
        }.count { it }
    }

    private fun createRuleMap(input: List<String>): Map<String,String> {
        var i = 0
        val ruleMap = mutableMapOf<String,String>()
        input.takeWhile { it.isNotBlank() }.forEach {
            val (key,value) = it.split(":")
            ruleMap[key] = value.trim()
        }
        return ruleMap
    }

    private fun generateRegex(rule: String, ruleMap: Map<String, String>): String {
        when {
            rule.contains('|') -> {
                val subrules = rule.split("|").map { it.trim() }
                return "(" + generateRegex(subrules[0], ruleMap) + "|" + generateRegex(subrules[1], ruleMap) + ")"
            }
            rule.contains(' ') -> {
                return rule.split(" ").map { it.trim() }.joinToString("") {
                    val result = generateRegex(it, ruleMap)
                    result
                }
            }
            rule.contains('"') -> {
                return rule.substring(1, 2)
            }
            else -> {
                return generateRegex(ruleMap[rule] ?: error("no rule"), ruleMap)
            }
        }
    }

    private fun validateInput(input: String, ruleMap: Map<String,String>, rules: List<String>): Boolean {
        if (input.isBlank()) {
            return rules.isEmpty()
        } else if (rules.isEmpty()) {
            return false
        }

        return (ruleMap[rules[0]] ?: error("no rule for $rules[0]")).let { rule ->
            when {
                rule.contains('"') ->
                    if (input[0] == rule[1]) {
                        validateInput(input.substring(1), ruleMap, rules.drop(1))
                    } else {
                        false
                    }
                else -> {
                    rule.split("|").map { it.trim() }.any { subrule ->
                        validateInput(input, ruleMap, subrule.split(" ") + rules.drop(1))
                    }
                }
            }
        }
    }

    override fun partTwo(): Any {
        val ruleMap = mutableMapOf<String,String>()
        ruleMap.putAll(createRuleMap(inputList))
        ruleMap["8"] = "42 | 42 8"
        ruleMap["11"] = "42 31 | 42 11 31"

        return inputList.subList(inputList.indexOfFirst { it.isBlank() } + 1, inputList.size).count {
            validateInput(it, ruleMap, listOf("0"))
        }
    }
}