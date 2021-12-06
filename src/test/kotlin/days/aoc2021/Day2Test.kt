package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day2Test {
    val day = Day2()
    val input =
"""
forward 5
down 5
forward 8
up 3
down 8
forward 2
""".trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePositionAndMultiply(input), Is.`is`(150))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateAimPositionAndMultiply(input), Is.`is`(900))
    }
}