package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day11Test {

    private val day = Day11()

    @Test
    fun testPartOne() {
        assertThat(day.validatePassword("abcdffaa"), `is`(true))
        assertThat(day.validatePassword("ghjaabcc"), `is`(true))
        assertThat(day.validatePassword("hijklmmn"), `is`(false))
        assertThat(day.validatePassword("abbceffg"), `is`(false))
        assertThat(day.validatePassword("abbcegjk"), `is`(false))

        assertThat(day.partOne(), `is`("ghjaabcc"))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`("ghjbbcdd"))
    }
}
