package util

import java.util.PriorityQueue

class Pathfinding<VertexType> {

    fun interface NeighborFilter <VertexType> {
        fun filter(current: VertexType, neighbor: VertexType): Boolean
    }

    fun dfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
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
        neighborFilter: NeighborFilter<VertexType>,
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
        neighborFilter: NeighborFilter<VertexType>,
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
            neighborIterator(current.first).filter { it !in visited && neighborFilter.filter(current.first, it) }
                .forEach {
                    potentialPath.addFirst(
                        Pair(
                            it,
                            current.second.plus(current.first).toMutableList()
                        )
                    )
                }
        }

        return listOf()
    }

    fun dijkstraShortestPath(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
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
                break
            } else {
                neighborIterator(current.first).filter { neighborFilter.filter(current.first, it) }.forEach { neighbor ->
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

    fun findAllPaths(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        terminationCondition: (VertexType) -> Boolean
    ): List<List<VertexType>> {
        val completedPaths = mutableListOf<List<VertexType>>()
        val potentialPath = ArrayDeque<Pair<VertexType, List<VertexType>>>()
        potentialPath.addFirst(Pair(start, listOf()))

        while (potentialPath.isNotEmpty()) {
            val current = potentialPath.removeFirst()
            if (terminationCondition(current.first)) {
                completedPaths.add(current.second.plus(current.first))
            } else {
                neighborIterator(current.first).filter { neighborFilter.filter(current.first, it) }
                    .filter { !current.second.contains(it) }.forEach {
                    potentialPath.addLast(it to current.second.plus(current.first))
                }
            }
        }

        return completedPaths
    }

    fun longestPath(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        terminationCondition: (VertexType) -> Boolean
    ): List<VertexType>? {
        var longestPath: List<VertexType>? = null
        val potentialPath = ArrayDeque<Pair<VertexType, List<VertexType>>>()
        potentialPath.addFirst(Pair(start, listOf()))

        while (potentialPath.isNotEmpty()) {
            val current = potentialPath.removeFirst()
            if (terminationCondition(current.first)) {
                current.second.plus(current.first).let {
                    if ((longestPath?.size ?: 0) < it.size) {
                        longestPath = it
                    }
                }
            } else {
                neighborIterator(current.first).filter { neighborFilter.filter(current.first, it) }
                    .filter { !current.second.contains(it) }.forEach {
                        potentialPath.addFirst(it to current.second.plus(current.first))
                    }
            }
        }

        return longestPath
    }
}