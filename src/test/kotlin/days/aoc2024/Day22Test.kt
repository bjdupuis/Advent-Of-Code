package days.aoc2024

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day22Test {
    val day = Day22()
    val input = """
1
10
100
2024
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(37327623))
    }

    val input2 = """
1
2
3
2024
    """.trimIndent().lines()
    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input2), `is`(23))
    }
}