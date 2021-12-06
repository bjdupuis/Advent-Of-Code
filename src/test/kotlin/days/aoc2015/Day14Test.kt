package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day14Test {

    private val day = Day14()

    @Test
    fun testPartOne() {
        val list =
"""
Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
""".trimIndent().lines()

        val reindeer = day.parseReindeer(list)
        assertThat(reindeer.map { it.calculateDistanceForTime(1000) }.maxOrNull(), `is`(1120))
    }

    @Test
    fun testPartTwo() {
        val list =
                """
Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
""".trimIndent().lines()

        val reindeer = day.parseReindeer(list)
        assertThat(day.calculateWinningScore(reindeer, 1000), `is`(689))
    }
}
