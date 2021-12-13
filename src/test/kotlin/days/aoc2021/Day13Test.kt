package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day13Test {
    val day = Day13()
    val inputList = """
6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateDotsAfterFirstFold(inputList), `is`(17))
    }


    private val expected = """
#####
#...#
#...#
#...#
#####
.....
.....
    """.trimIndent().lines().toTypedArray()

    @Test
    fun testPartTwo() {
        val result = day.calculateDotsAfterFolds(inputList).map { line ->
            line.joinToString("")
        }.toTypedArray()


        MatcherAssert.assertThat(expected.contentEquals(result), `is`(true))
    }
}