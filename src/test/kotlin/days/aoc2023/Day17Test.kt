package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day17Test {
    val day = Day17()
    val input = """
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(102))
    }

    val input2 = """
111111111111
999999999991
999999999991
999999999991
999999999991
    """.trimIndent().lines()

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(94))
        MatcherAssert.assertThat(day.calculatePartTwo(input2), `is`(71))
    }
}