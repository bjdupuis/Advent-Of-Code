package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day3Test {

    private val day = Day3()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(7L))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.performCount(1,1), `is`(2))
        assertThat(day.performCount(3,1), `is`(7))
        assertThat(day.performCount(5,1), `is`(3))
        assertThat(day.performCount(7,1), `is`(4))
        assertThat(day.performCount(1,2), `is`(2))
        assertThat(day.partTwo(), `is`(336L))
    }
}
