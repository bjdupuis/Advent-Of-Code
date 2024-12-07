package util

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.junit.Test

class ExtensionsTest {
    @Test
    fun `combinations provides combinations `() {
        val list = listOf(10, 20, 30, 40, 50)

        list.combinations(3).forEach { combination ->
            println("$combination")
        }
    }

    @Test
    fun `permutations do things`() {
        val list = listOf(10, 20, 30, 40, 50)

        list.permutations(2, 5).forEach { permutation ->
            println("$permutation")
        }

        MatcherAssert.assertThat(list.permutations(3).count(), `is`(5 * 4 * 3))
        MatcherAssert.assertThat(list.permutations(2, 5).count(), `is`(5 * 4 + 5 * 4 * 3 + 5 * 4 * 3 * 2 + 5 * 4 * 3 * 2))
    }
}