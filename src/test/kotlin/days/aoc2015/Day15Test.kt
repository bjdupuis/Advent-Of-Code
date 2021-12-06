package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day15Test {

    private val day = Day15()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(62842880))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(57600000))
    }
}
