package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day9Test {
    val day = Day9()
    val inputLines = """
2199943210
3987894921
9856789892
8767896789
9899965678        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateRiskLevelSum(inputLines), Is.`is`(15))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateThreeLargestBasins(inputLines), Is.`is`(1134))
    }
}