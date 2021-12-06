package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day1Test {

    private val dayOne = Day1()

    @Test
    fun testPartOne() {
        assertThat(dayOne.partOne(), `is`(514579))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayOne.partTwo(), `is`(241861950))
    }
}
