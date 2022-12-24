package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day23Test {
    val day = Day23()
    val input = """
....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateEmptyLandOnMinimalRectangleAfterRounds(input, 10), `is`(110))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateRoundWithNoMovingElves(input), `is`(20))
    }
}