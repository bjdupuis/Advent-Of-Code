package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day21Test {
    val day = Day21()

    @Test
    fun testPartOne() {
        val inputLines = """
            Player 1 starting position: 4
            Player 2 starting position: 8
        """.trimIndent().lines()

        MatcherAssert.assertThat(day.calculateScoresForGame(inputLines), `is`(739785L))
    }

    @Test
    fun testPartTwo() {
        val inputLines = """
            Player 1 starting position: 4
            Player 2 starting position: 8
        """.trimIndent().lines()

        MatcherAssert.assertThat(day.calculateDiracWinners(inputLines), `is`(444356092776315L))
    }
}