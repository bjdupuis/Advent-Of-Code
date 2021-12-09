package days.aoc2021

import days.Day

class Day8 : Day(2021, 8) {
    override fun partOne(): Any {
        return calculateFrequencyOfCertainOutputs(inputList)
    }

    override fun partTwo(): Any {
        return calculateOutputValueSum(inputList)
    }

    fun calculateFrequencyOfCertainOutputs(inputLines: List<String>): Int {
        val uniqueSizes = setOf(2, 3, 4, 7)
        return inputLines.sumBy { line ->
            line.substringAfter('|').trim().split("\\s".toRegex()).filter {
                it.length in uniqueSizes
            }.count()
        }
    }

    fun calculateOutputValueSum(inputLines: List<String>): Int {
        return inputLines.sumBy { line ->
            line.substringBefore('|').split("\\s".toRegex()).map { it.toSet() }.let { sets ->
                val a = sets.first { it.size == 3 }.minus(sets.first { it.size == 2}).first()
                val f = sets.filter { it.size == 6 }.reduce { a, b -> a intersect b }.intersect(sets.first { it.size == 2 }).first()
                val c = sets.first { it.size == 2 }.minus(setOf(f)).first()
                val d = sets.filter { it.size == 5 }.reduce { a, b -> a intersect b }.intersect(sets.first { it.size == 4 }).minus(setOf(c, f)).first()
                val b = sets.first { it.size == 4 }.minus(setOf(c, d, f)).first()
                val g = sets.filter { it.size == 6 }.reduce { a, b -> a intersect b }.minus(setOf(a, f, c, d, b)).first()
                val e = sets.first { it.size == 7 }.minus(setOf(a, b, c, d, f, g)).first()

                val signalToValues = listOf(
                    setOf(a, b, c, e, f, g),
                    setOf(c, f),
                    setOf(a, c, d, e, g),
                    setOf(a, c, d, f, g),
                    setOf(b, c, d, f),
                    setOf(a, b, d, f, g),
                    setOf(a, b, d, e, f, g),
                    setOf(a, c, f),
                    setOf(a, b, c, d, e, f, g),
                    setOf(a, b, c, d, f, g)
                )

                signalToValues.forEachIndexed { index, s -> println("For $index the signals are $s") }

                var multiplier = 1000
                line.substringAfter('|').trim().split("\\s".toRegex()).map { s ->
                    val current = s.toCharArray().toSet()
                    val value = signalToValues.indexOf(current) * multiplier

                    println ("For $s value is $value")
                    multiplier /= 10
                    value
                }.sum()

            }
        }

        /*
        return inputLines.sumBy { line ->
            var multiplier = 1000
            line.substringAfter('|').trim().split("\\s".toRegex()).map { s ->
                val string = s.toCharArray().sorted().joinToString("")
                val value = signalToValueMap.indexOf(string) * multiplier
                multiplier /= 10
                value
            }.sum()
        }
         */
        return 0
    }
}