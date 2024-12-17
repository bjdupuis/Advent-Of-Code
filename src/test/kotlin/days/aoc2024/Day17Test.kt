package days.aoc2024

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day17Test {
    val day = Day17()
    val input = """
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`("4,6,3,5,6,3,5,2,1,0"))
    }

    val input2 = """
Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0
    """.trimIndent().lines()

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input2), `is`(117440))
    }
}