package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day17Test {
    val day = Day17()
    val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateHeightOfTowerAfterRocks(input, 2022), `is`(3068))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateHeightWithPattern(input, 1000000000000L), `is`(1514285714288L))
    }
}