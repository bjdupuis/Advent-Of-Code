package days.aoc2023

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day10Test {
    val day = Day10()
    val input1 = """
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
    """.trimIndent().lines()
    val input2 = """
-L|F7
7S-7|
L|7||
-L-J|
L|-JF
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input1), `is`(8))
        MatcherAssert.assertThat(day.calculatePartOne(input2), `is`(4))
    }

    val input3 = """
...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
    """.trimIndent().lines()
    val input4 = """
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
    """.trimIndent().lines()
    val input5 = """
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
    """.trimIndent().lines()

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input3), `is`(4))
        MatcherAssert.assertThat(day.calculatePartTwo(input4), `is`(8))
        MatcherAssert.assertThat(day.calculatePartTwo(input5), `is`(10))
    }
}