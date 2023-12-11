package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day11Test {
    val day = Day11()
    val input = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(374))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input, 2), `is`(374L))
        MatcherAssert.assertThat(day.calculatePartTwo(input, 10), `is`(1030L))
        MatcherAssert.assertThat(day.calculatePartTwo(input, 100), `is`(8410L))
    }
}