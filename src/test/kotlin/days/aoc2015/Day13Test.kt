package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day13Test {

    private val day = Day13()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(330))
    }

    @Test
    fun testPartTwo() {
    }
}
