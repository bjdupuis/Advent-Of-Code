package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day6Test {
    val day = Day6()
    val input = """
3,4,3,1,2
""".trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateFishAfterDays(18, input), Is.`is`(26))
        MatcherAssert.assertThat(day.calculateFishAfterDays(80, input), Is.`is`(5934))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateFishAfterDays(256, input), Is.`is`(26984457539))
    }

}