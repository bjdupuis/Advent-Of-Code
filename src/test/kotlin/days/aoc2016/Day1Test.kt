package days.aoc2016

import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day1Test {
    val day = Day1()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateDistanceForDirections("R2, L300"), `is`(302))
        MatcherAssert.assertThat(day.calculateDistanceForDirections("R2, R2, R2"), `is`(2))
        MatcherAssert.assertThat(day.calculateDistanceForDirections("R5, L5, R5, R3"), `is`(12))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.findFirstLocationVisitedTwice("R8, R4, R4, R8"), `is`(4))
    }
}