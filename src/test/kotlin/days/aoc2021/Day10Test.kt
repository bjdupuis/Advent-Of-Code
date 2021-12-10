package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day10Test {
    val day = Day10()
    val inputLines = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateTotalSyntaxError(inputLines), `is`(26397))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateMiddleCompleteScore(inputLines), `is`(288957))
    }
}