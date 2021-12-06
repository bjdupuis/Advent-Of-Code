package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day24Test {

    private val day = Day24()

    @Test
    fun testPartOne() {
        val shorter =
"""
nwwswee
esew
""".trimIndent().lines()
        val grid = day.calculateStartingGrid(shorter)
        val tile1 = grid.getOrInsertDefault(Triple(0,0,0), HexTile())
        val tile2 = grid.getOrInsertDefault(Triple(0,-1,1), HexTile())
        assertThat(tile1.color, `is`(HexTile.Color.BLACK))
        assertThat(tile2.color, `is`(HexTile.Color.BLACK))
        assertThat(day.partOneSolution(shorter), `is`(2))
        println()
        assertThat(day.partOne(), `is`(10))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(2208))
    }
}
