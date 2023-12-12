package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day12Test {
    val day = Day12()
    val input = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
    """.trimIndent().lines()

    @Test
    fun testUtilities() {
        MatcherAssert.assertThat(day.isValidCondition("#.#.###", listOf(1,1,3)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition(".#...#....###.", listOf(1,1,3)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition(".#.###.#.######", listOf(1,3,1,6)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition("####.#...#...", listOf(4,1,1)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition("#....######..#####.", listOf(1,6,5)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition(".###.##....#", listOf(3,2,1)), `is`(true))
        MatcherAssert.assertThat(day.isValidCondition("#.##.###", listOf(1,1,3)), `is`(false))
        MatcherAssert.assertThat(day.isValidCondition(".#...#....###.#", listOf(1,1,3)), `is`(false))
        MatcherAssert.assertThat(day.isValidCondition(".#.###.#.#####", listOf(1,3,1,6)), `is`(false))
        MatcherAssert.assertThat(day.isValidCondition("###.#...#...", listOf(4,1,1)), `is`(false))
        MatcherAssert.assertThat(day.isValidCondition("##....######..#####.", listOf(1,6,5)), `is`(false))
        MatcherAssert.assertThat(day.isValidCondition(".###.##.....", listOf(3,2,1)), `is`(false))
    }

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(21))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(525152L))
    }
}