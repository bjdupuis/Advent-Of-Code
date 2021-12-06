package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day21Test {

    private val day = Day21()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(5))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`("mxmxvkd,sqjhc,fvjkl"))
    }
}
