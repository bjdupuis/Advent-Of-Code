package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day6Test {
    val day = Day6()
    val input1 = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.charactersBeforeStartOfMarker(input1, 4), `is`(7))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.charactersBeforeStartOfMarker(input1, 14), `is`(19))
    }
}