package days.aoc2025

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day6Test {
    val day = Day6()
    val input = """
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(3, input), `is`(4277556L))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(3, input), `is`(3263827L))
    }
}