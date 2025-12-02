package days.aoc2025

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day2Test {
    val day = Day2()
    val input = """
11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(1227775554L))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(4174379265L))
    }
}