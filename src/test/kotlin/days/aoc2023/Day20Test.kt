package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day20Test {
    val day = Day20()
    val input = """
broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
    """.trimIndent().lines()

    val input2 = """
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(32000000))
        MatcherAssert.assertThat(day.calculatePartOne(input2), `is`(11687500))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(0))
    }
}