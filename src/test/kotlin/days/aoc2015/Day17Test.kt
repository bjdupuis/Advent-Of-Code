package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day17Test {

    private val day = Day17()

    @Test
    fun testPartOne() {
        assertThat(day.calculatePartOne(25), `is`(4))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.calculatePartTwo(25), `is`(3))
    }
}
