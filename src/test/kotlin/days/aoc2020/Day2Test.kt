package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day2Test {

    private val dayTwo = Day2()

    @Test
    fun testPartOne() {
        assertThat(dayTwo.partOne(), `is`(2))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayTwo.partTwo(), `is`(1))
    }
}
