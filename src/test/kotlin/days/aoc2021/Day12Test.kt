package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day12Test {
    val day = Day12()

    val input1 = """
start-A
start-b
A-c
A-b
b-d
A-end
b-end        
    """.trimIndent().lines()

    val input2 = """
dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc        
    """.trimIndent().lines()

    val input3 = """
fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateNumberOfPaths(input1), `is`(10))
        MatcherAssert.assertThat(day.calculateNumberOfPaths(input2), `is`(19))
        MatcherAssert.assertThat(day.calculateNumberOfPaths(input3), `is`(226))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateNumberOfPathsVisitSmallCavesTwice(input1), `is`(36))
        MatcherAssert.assertThat(day.calculateNumberOfPathsVisitSmallCavesTwice(input2), `is`(103))
        MatcherAssert.assertThat(day.calculateNumberOfPathsVisitSmallCavesTwice(input3), `is`(3509))
    }
}