package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day17Test {
    private val day = Day17()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.findHighestY("target area: x=20..30, y=-10..-5"), `is`(45))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.countAllPossibleTrajectories("target area: x=20..30, y=-10..-5"), `is`(112))
    }
}