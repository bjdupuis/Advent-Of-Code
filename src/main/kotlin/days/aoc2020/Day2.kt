package days.aoc2020

import days.Day

class Day2 : Day(2020, 2) {
    override fun partOne(): Any {
        return inputList.filter { line ->
            Regex("([0-9]+)-([0-9]+) ([a-z]): (\\w+)").matchEntire(line)?.destructured?.let { (min, max, character, password) ->
                password.count { value -> value == character.first() } in min.toInt()..max.toInt()
            } ?: false
        }.count()
    }

    override fun partTwo(): Any {
        return inputList.filter { line ->
            Regex("([0-9]+)-([0-9]+) ([a-z]): (\\w+)").matchEntire(line)?.destructured?.let { (first, second, character, password) ->
                (password[first.toInt() - 1] == character.first()) xor (password[second.toInt() - 1] == character.first())
            } ?: false
        }.count()
    }

}