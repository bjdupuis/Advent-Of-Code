package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day13Test {

    private val day = Day13()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(295))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwoAnswer("17,x,13,19"), `is`(3417))
        assertThat(day.partTwoAnswer("67,7,59,61"), `is`(754018))
        assertThat(day.partTwoAnswer("67,x,7,59,61"), `is`(779210))
        assertThat(day.partTwoAnswer("67,7,x,59,61"), `is`(1261476))
        assertThat(day.partTwoAnswer("1789,37,47,1889"), `is`(1202161486))
    }
}
