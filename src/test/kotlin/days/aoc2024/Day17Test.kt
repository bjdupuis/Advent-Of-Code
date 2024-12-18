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
        println(
        day.runCompleteProgram(mutableMapOf(
            'A' to 15401768L,
            'B' to 0L,
            'C' to 0L
        ), "2,4,1,7,7,5,0,3,4,4,1,7,5,5,3,0".split(",")))
        //MatcherAssert.assertThat(day.calculatePartTwo(input2), `is`(117440))
    }
}