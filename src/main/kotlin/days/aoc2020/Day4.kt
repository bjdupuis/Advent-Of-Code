package days.aoc2020

import days.Day

class Day4: Day(2020, 4) {

    override fun partOne(): Any {
        val requiredKeys = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
        return parsePassports().filter { passport ->
            passport.keys.containsAll(requiredKeys)
        }.count()
    }

    private fun parsePassports(): List<Map<String, String>> {
        var currentPassportDescription: String = ""
        val passports: MutableList<Map<String, String>> = mutableListOf()

        inputList.forEach { line ->
            if (line.isBlank()) {
                if (currentPassportDescription.isNotBlank()) {
                    passports.add(createPassport(currentPassportDescription))
                }
                currentPassportDescription = ""
            } else {
                currentPassportDescription += "$line "
            }
        }
        passports.add(createPassport(currentPassportDescription))

        return passports
    }

    private fun createPassport(line: String): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()
        Regex("(\\w+):(\\S*) ").findAll(line).forEach {
            it.destructured.let { (key, value) ->
                resultMap[key] = value
            }
        }

        return resultMap
    }

    override fun partTwo(): Any {
        return parsePassports().filter { passport ->
            isValid(passport)
        }.count()
    }

    private fun isValid(passport: Map<String, String>): Boolean {
        passport.entries.forEach { (key, value) ->
            when(key) {
                "byr" -> {
                    value.toInt().let {
                        if (it !in 1920..2002)
                            return false
                    }
                }
                "iyr" -> {
                    value.toInt().let {
                        if (it !in 2010..2020)
                            return false
                    }
                }
                "eyr" -> {
                    value.toInt().let {
                        if (it !in 2020..2030)
                            return false
                    }
                }
                "hgt" -> {
                    when {
                        value.endsWith("cm") -> {
                            value.substringBefore('c').toInt().let {
                                if (it !in 150..193)
                                    return false
                            }
                        }
                        value.endsWith("in") -> {
                            value.substringBefore('i').toInt().let {
                                if (it !in 59..76)
                                    return false
                            }
                        }
                        else -> {
                            return false
                        }
                    }
                }
                "hcl" -> {
                    if (!Regex("#([0-9a-f]){6}").matches(value)) {
                        return false
                    }
                }
                "ecl" -> {
                    if (!setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(value)) {
                        return false
                    }
                }
                "pid" -> {
                    if (!Regex("\\d{9}").matches(value)) {
                        return false
                    }
                }
            }
        }

        val requiredKeys = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        return passport.keys.containsAll(requiredKeys)
    }
}