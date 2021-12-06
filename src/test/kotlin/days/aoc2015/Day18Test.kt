package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day18Test {

    private val day = Day18()

    @Test
    fun testPartOne() {
        val inputList =
"""
.#.#.#
...##.
#....#
..#...
#.#..#
####..
""".trimIndent().lines()

        val result = day.performLife(inputList,4)
        assertThat(result.map { it.count { it == '#' } }.sum(), `is`(4))
    }

    @Test
    fun testPartTwo() {
    }
}
