package util

import java.util.PriorityQueue

class Pathfinding<VertexType> {
    fun dfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        terminationCondition: (VertexType) -> Boolean
    ): List<VertexType> {
        return dfs(start, neighborIterator, { true }, terminationCondition)
    }

    fun dfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: (VertexType) -> Boolean,
        terminationCondition: (VertexType) -> Boolean
    ): List<VertexType> {
        val potentialPath = ArrayDeque<Pair<VertexType, MutableList<VertexType>>>()

        potentialPath.addFirst(Pair(start, mutableListOf()))
        return iteratePath(
            potentialPath,
            neighborIterator,
            neighborFilter,
            terminationCondition,
            potentialPath::removeFirst
        )
    }

    fun bfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        terminationCondition: (VertexType) -> Boolean
    ): List<VertexType> {
        return bfs(start, neighborIterator, { true }, terminationCondition)
    }

    fun bfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: (VertexType) -> Boolean,
        terminationCondition: (VertexType) -> Boolean
    ): List<VertexType> {
        val potentialPath = ArrayDeque<Pair<VertexType, MutableList<VertexType>>>()

        potentialPath.addFirst(Pair(start, mutableListOf()))
        return iteratePath(
            potentialPath,
            neighborIterator,
            neighborFilter,
            terminationCondition,
            potentialPath::removeLast
        )
    }

    private fun iteratePath(
        potentialPath: ArrayDeque<Pair<VertexType, MutableList<VertexType>>>,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: (VertexType) -> Boolean,
        terminationCondition: (VertexType) -> Boolean,
        extractor: () -> Pair<VertexType, MutableList<VertexType>>
    ): List<VertexType> {

        val visited = mutableSetOf<VertexType>()
        while (potentialPath.isNotEmpty()) {
            val current = extractor()
            if (terminationCondition(current.first)) {
                return current.second.plus(current.first)
            }

            visited.add(current.first)
            neighborIterator(current.first).filter { it !in visited }.filter(neighborFilter).forEach {
                potentialPath.addFirst(Pair(it, current.second.plus(current.first).toMutableList()))
            }
        }

        return listOf()
    }

    fun dijkstraShortestPath(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: (VertexType) -> Boolean,
        edgeCost: (VertexType, VertexType) -> Int,
        terminationCondition: (VertexType) -> Boolean
    ): Int {
        val frontier = PriorityQueue(compareBy<Pair<VertexType, Int>> { it.second })
        val costSoFar = mutableMapOf<VertexType, Int>()
        val cameFrom = mutableMapOf<VertexType, VertexType?>()
        cameFrom[start] = null
        costSoFar[start] = 0
        frontier.add(start to 0)
        var current: Pair<VertexType, Int>? = null
        while (frontier.isNotEmpty()) {
            current = frontier.poll()
            if (terminationCondition(current.first)) {
                break;
            } else {
                neighborIterator(current.first).filter(neighborFilter).forEach { neighbor ->
                    val cost = costSoFar[current.first]!!.plus(edgeCost(current.first, neighbor))
                    if (!costSoFar.containsKey(neighbor) || cost < costSoFar[neighbor]!!) {
                        costSoFar[neighbor] = cost
                        frontier.add(neighbor to cost)
                        cameFrom[neighbor] = current.first
                    }
                }
            }
        }

        return costSoFar[current!!.first]!!
    }

}

