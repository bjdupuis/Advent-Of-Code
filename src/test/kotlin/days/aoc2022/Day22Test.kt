package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day22Test {
    val day = Day22()
    val input = """        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5""".lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateFinalPassword(input), `is`(6032))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateFinalPasswordOnCube(input, 4), `is`(5031))
    }
}