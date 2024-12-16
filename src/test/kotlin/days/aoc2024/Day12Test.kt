package days.aoc2024

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day12Test {
    val day = Day12()
    val input = """
AAAA
BBCD
BBCC
EEEC
    """.trimIndent().lines()

    val input2 = """
OOOOO
OXOXO
OOOOO
OXOXO
OOOOO
    """.trimIndent().lines()

    val input3 = """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
    """.trimIndent().lines()

    val input4 = """
EEEEE
EXXXX
EEEEE
EXXXX
EEEEE
    """.trimIndent().lines()

    val input5 = """
OOOOO
OXOXO
OXXXO
    """.trimIndent().lines()

    val input6 = """
AAAAAAAA
AOOAAAOA
AOOAAAOA
OOOAAOAA
    """.trimIndent().lines()


    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(140))
        MatcherAssert.assertThat(day.calculatePartOne(input2), `is`(772))
        MatcherAssert.assertThat(day.calculatePartOne(input3), `is`(1930))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(80))
        MatcherAssert.assertThat(day.calculatePartTwo(input2), `is`(436))
        MatcherAssert.assertThat(day.calculatePartTwo(input4), `is`(236))
        MatcherAssert.assertThat(day.calculatePartTwo(input3), `is`(1206))
        MatcherAssert.assertThat(day.calculatePartTwo(input5), `is`(160))
        MatcherAssert.assertThat(day.calculatePartTwo(input6), `is`(406))
    }
}