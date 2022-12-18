package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day18Test {
    val day = Day18()
    val input = """
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.countExposedSidesOfCubes(input), `is`(64))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.countExposedExteriorSidesOfCubes(input), `is`(58))
    }
}