package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day4Test {
    val day = Day4()
    val input = """
2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8        
    """.trimIndent().trim().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.countFullyContainedRanges(input), `is`(2))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.countIntersectingRanges(input), `is`(4))
    }
}