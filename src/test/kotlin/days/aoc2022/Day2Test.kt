package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day2Test {
    val day = Day2()
    val input = """
A Y
B X
C Z
    """.trimIndent().trim().lines()


    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateScoreOfGame1(input), Is.`is`(15))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateScoreOfGame2(input), Is.`is`(12))
    }
}