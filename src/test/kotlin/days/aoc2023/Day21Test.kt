package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day21Test {
    val day = Day21()
    val input = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePossiblePlotsForSteps(input, 6), `is`(16))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePossiblePlotsForStepsOnInfinitePlane(input, 6), `is`(16))
    }
}