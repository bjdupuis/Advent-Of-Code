package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day25Test {

    private val day = Day25()

    @Test
    fun testPartOne() {
        assertThat(day.findLoopSize(7, 5764801L), `is`(8))
        assertThat(day.transform(7, 8), `is`(5764801L))
        assertThat(day.partOne(), `is`(14897079L))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(true))
    }
}
