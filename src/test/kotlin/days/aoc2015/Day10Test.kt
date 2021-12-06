package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day10Test {

    private val day = Day10()

    @Test
    fun testPartOne() {
        assertThat(day.calculateLookAndSay("1"), `is`("11"))
        assertThat(day.calculateLookAndSay("11"), `is`("21"))
        assertThat(day.calculateLookAndSay("21"), `is`("1211"))
        assertThat(day.calculateLookAndSay("1211"), `is`("111221"))
        assertThat(day.calculateLookAndSay("111221"), `is`("312211"))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(4666278))
    }
}
