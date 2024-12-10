package days.aoc2024

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day10Test {
    val day = Day10()
    val input = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
    """.trimIndent().lines()

    val input2 = """
...0...
...1...
...2...
6543456
7.....7
8.....8
9.....9
    """.trimIndent().lines()

    val input3 = """
..90..9
...1.98
...2..7
6543456
765.987
876....
987....
    """.trimIndent().lines()

    val input4 = """
10..9..
2...8..
3...7..
4567654
...8..3
...9..2
.....01
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input2), `is`(2))
        MatcherAssert.assertThat(day.calculatePartOne(input3), `is`(4))
        MatcherAssert.assertThat(day.calculatePartOne(input4), `is`(3))
        MatcherAssert.assertThat(day.calculatePartOne(input), `is`(36))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input), `is`(81))
    }
}