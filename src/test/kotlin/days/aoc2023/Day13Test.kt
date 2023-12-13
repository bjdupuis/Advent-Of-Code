package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day13Test {
    val day = Day13()
    val input = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#

    """.trimIndent().lines()

    val input2 = """
.#.#....#.#.###
...#....#...#..
....####....#..
.#.######.#..##
###..##..###...
.#...##...#.###
..##....##.....
.#...##...#..##
..##....##..#..
.##########....
##.#.##.#.##...
....####.....##
##...##...##.##
##.#.##.#.##.##
...#....#...###
##...##...#####
....#..........
    """.trimIndent().lines()

    val input3 = """
.#..##..#.##.#.
...###.#..##..#
.##.....#....#.
...#...###..###
.##.#..#.#..#.#
.##.##.###..#.#
##.##..########
###....#.#..#.#
.#...##.######.
##.#..#.######.
#.#..##..####..
######..##..##.
#.#..####....##
#.##.#.#.#..#.#
.#...#..#.##.#.
.#...#..#.##.#.
#.##.#.#.#..#.#
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(405))
        MatcherAssert.assertThat(day.calculatePartOne(input2), `is`(14))
        MatcherAssert.assertThat(day.calculatePartOne(input3), `is`(1500))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(400))
        MatcherAssert.assertThat(day.calculatePartTwo(input3), `is`(11))
    }
}