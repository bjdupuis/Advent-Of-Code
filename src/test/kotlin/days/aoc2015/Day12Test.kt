package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day12Test {

    private val day = Day12()

    @Test
    fun testPartOne() {
        assertThat(day.calculateSumOfNumbers("[1,2,3]{\"a\":2,\"b\":4}"), `is`(12))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.calculateNonRedNumbersInObject("[1,2,3]", 0).first, `is`(6))
        assertThat(day.calculateNonRedNumbersInObject("[1,{\"c\":\"red\",\"b\":2},3]", 0).first, `is`(4))
        assertThat(day.calculateNonRedNumbersInObject("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}", 0).first, `is`(0))
    }
}
