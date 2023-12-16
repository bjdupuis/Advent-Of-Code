package util

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class PathfindingTest {

    val input = """
....OOO.O...
..S.O.OOOO..
..OOOO.O.E..
..O..OOO....
..OOOO......
    """.trimIndent().lines()

    @Test
    fun `test that the pathfinder can do a dfs`() {
        val pathfinding = Pathfinding<Point2d>()
        val map = CharArray2d(input)
        val path = pathfinding.dfs(
            map.findFirst('S')!!,
            Point2d::neighbors,
            { point -> point.isWithin(map) && map[point] != '.' }) { map[it] == 'E' }
        MatcherAssert.assertThat(path.last(), `is`(Point2d(9, 2)))
        MatcherAssert.assertThat(path.size, `is`(19))
    }

    @Test
    fun `test that the pathfinder can do a bfs`() {
        val pathfinding = Pathfinding<Point2d>()
        val map = CharArray2d(input)
        val path = pathfinding.bfs(
            map.findFirst('S')!!,
            Point2d::neighbors,
            { point -> point.isWithin(map) && map[point] != '.' }) { map[it] == 'E' }
        MatcherAssert.assertThat(path.last(), `is`(Point2d(9, 2)))
        MatcherAssert.assertThat(path.size, `is`(13))
    }

    @Test
    fun `test that the pathfinder can do a bfs with diagonals`() {
        val pathfinding = Pathfinding<Point2d>()
        val map = CharArray2d(input)
        val path = pathfinding.bfs(
            map.findFirst('S')!!,
            Point2d::allNeighbors,
            { point -> point.isWithin(map) && map[point] != '.' }) { map[it] == 'E' }
        MatcherAssert.assertThat(path.last(), `is`(Point2d(9, 2)))
        MatcherAssert.assertThat(path.size, `is`(8))
    }

}