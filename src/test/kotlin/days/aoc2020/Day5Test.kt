package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day5Test {

    private val day = Day5()

    @Test
    fun testPartOne() {
        assertThat(day.calculateSeatId("FBFBBFFRLR"), `is`(357))
        assertThat(day.calculateSeatId("BFFFBBFRRR"), `is`(567))
        assertThat(day.calculateSeatId("FFFBBBFRRR"), `is`(119))
        assertThat(day.calculateSeatId("BBFFBBFRLL"), `is`(820))
        assertThat(day.partOne(), `is`(820))
    }

    @Test
    fun testPartTwo() {
    }
}
