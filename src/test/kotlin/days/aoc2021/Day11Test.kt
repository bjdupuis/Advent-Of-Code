package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day11Test {
    val day = Day11()
    private val inputLines = """
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateFlashesAfterSteps(inputLines, 2), `is`(35))
        MatcherAssert.assertThat(day.calculateFlashesAfterSteps(inputLines, 10), `is`(204))
        MatcherAssert.assertThat(day.calculateFlashesAfterSteps(inputLines, 100), `is`(1656))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateFirstSimultaneousFlashStep(inputLines), `is`(195))
    }
}