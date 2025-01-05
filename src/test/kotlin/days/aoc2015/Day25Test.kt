package days.aoc2015

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day25Test {
    val day = Day25()
    val input = "4,3"

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(21345942))
    }

    @Test
    fun testPartTwo() {
    }
}