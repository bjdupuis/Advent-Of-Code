package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day8Test {
    val day = Day8()
    val input = """
30373
25512
65332
33549
35390
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateVisibleTrees(input), `is`(21))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateHighestScenicScore(input), `is`(8))
    }
}