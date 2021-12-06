package days.aoc2021

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day5Test {
    val day = Day5()
    val input = """
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
""".trimIndent().lines()

    @Test
    fun testPartOne() {
        val map = day.createMapWithNonDiagonals(input)
        MatcherAssert.assertThat(day.calculateMostDangerousPoints(map), Is.`is`(5))
    }

    @Test
    fun testPartTwo() {
        val map = day.createMapIncludingDiagonals(input)
        day.printMap(map)
        MatcherAssert.assertThat(day.calculateMostDangerousPoints(map), Is.`is`(12))
    }

}