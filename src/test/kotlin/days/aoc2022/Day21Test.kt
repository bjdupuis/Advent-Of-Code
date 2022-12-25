package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day21Test {
    val day = Day21()
    val input = """
root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculateWhatRootYells(input), `is`(152))
    }

    /*

27114486018320
27114486018312

27114486393056

27077010743728
27077010743720
26739729899568
     */
    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateWhatHumanYells(input), `is`(301))
    }
}