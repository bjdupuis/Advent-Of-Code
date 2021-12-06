package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day7Test {

    private val day = Day7()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(4))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(32))

        val list = """
shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
""".lines()

        val bags = day.createBagList(list)
        assertThat(day.countDescendents(bags["shiny gold"]!!), `is`(126))

    }
}
