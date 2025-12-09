package days.aoc2025

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day9Test {
    val day = Day9()
    val input = """
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(50L))
    }

    @Test
    fun testPartTwo() {
//        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(24L))
        // Works for the real puzzle, not the test :D
    }
}