package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day12Test {
    val day = Day12()
    val input = """
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateShortestRoute(input), `is`(31))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateShortestRouteFromAnyAToEnd(input), `is`(29))
    }
}