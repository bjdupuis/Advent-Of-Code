package days.aoc2016

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test
import util.Point2d

class Day8Test {
    val day = Day8()
    val input = """
rect 3x2
rotate column x=1 by 1
rotate row y=0 by 4
rotate column x=1 by 1
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input, Point2d(7,3)), `is`(6))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(0))
    }
}