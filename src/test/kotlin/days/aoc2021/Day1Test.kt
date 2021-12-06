package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day1Test {
    val partOneList =
"""
199
200
208
210
200
207
240
269
260
263
""".trimIndent().lines().map { it.toInt() }
    val day = Day1()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.countIncreasingPairs(partOneList), Is.`is`(7))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.countIncreasingTriples(partOneList), Is.`is`(5))
    }

}