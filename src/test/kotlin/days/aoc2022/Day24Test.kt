package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day24Test {
    val day = Day24()
    val input = """
#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#
    """.trimIndent().lines().filter { it.isNotBlank() }

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateFastestRoute(input), `is`(18))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateFastestRouteThereAndBackAndThereAgain(input), `is`(54))
    }
}