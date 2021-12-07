package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day7Test {
    val day = Day7()
    val input = """
16,1,2,0,4,2,7,1,2,14
""".trimIndent()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateCheapestFuel(input, true), Is.`is`(37))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateCheapestFuel(input, false), Is.`is`(168))
    }

}