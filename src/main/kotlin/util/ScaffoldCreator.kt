package util

import org.joda.time.DateTime
import java.io.File

object ScaffoldCreator {

    @JvmStatic
    fun main(args: Array<String>) {
        val (year, day) =
            if (args.size == 1 && "today" == args[0]) {
                listOf(DateTime.now().year.toString(), DateTime.now().dayOfMonth)
            } else {
                if (args.size != 2) {
                    printError("Must specify year and day")
                }
                listOf(args[0], args[1])
            }

        if (!File("./src/main/kotlin/days/aoc$year").exists()) {
            File("./src/main/kotlin/days/aoc$year").mkdir()
        }
        if (File("./src/main/kotlin/days/aoc$year/Day$day.kt").exists()) {
            throw IllegalStateException("Day$day.kt already exists in aoc$year")
        }

        if (File("./src/main/resources/$year/input_day_$day.txt").exists()) {
            throw IllegalStateException("input_day_$day.txt already exists")
        }

        if (File("./src/test/kotlin/days/aoc$year/Day${day}Test.kt").exists()) {
            throw IllegalStateException("Day${day}Test.kt already exists")
        }

        File("./src/main/kotlin/days/aoc$year/Day$day.kt").writeText(
            """
package days.aoc2023

import days.Day

class Day$day : Day($year, $day) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        return 0
    }

    fun calculatePartTwo(input: List<String>): Int {
        return 0
    }
}
        """.trimIndent()
        )

        File("./src/main/resources/$year/input_day_$day.txt").writeText("<replace this>")

        File("./src/test/kotlin/days/aoc$year/Day${day}Test.kt").writeText(
            """
package days.aoc$year

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day${day}Test {
    val day = Day$day()
    val input = ""${'"'}

    ""${'"'}.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(0))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(0))
    }
}
        """.trimIndent()
        )
    }

    private fun printError(message: String) {
        System.err.println("\n=== ERROR ===\n$message")
    }

}