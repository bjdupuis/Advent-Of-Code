package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day20Test {
    val day = Day20()
    val input = """
1
2
-3
3
-2
0
4
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateGroveCoordinateSum(input), `is`(3))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateGroveCoordinateSumWithDecryptionKey(input), `is`(1623178306L))
    }
}