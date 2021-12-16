package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day15Test {
    val day = Day15()
    private val inputList = """
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateLowestRiskPath(day.createCave(inputList)), `is`(40))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateLowestRiskPath(day.createFullCave(day.createCave(inputList))), `is`(315))
    }
}