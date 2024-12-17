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

    fun dijkstraShortestPathCost(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        terminationCondition: (VertexType) -> Boolean
    ): Int {
        var success = false
        val frontier = PriorityQueue(compareBy<Pair<VertexType, Int>> { it.second })
        val costSoFar = mutableMapOf<VertexType, Int>()
        costSoFar[start] = 0
        frontier.add(start to 0)
        var current: Pair<VertexType, Int>? = null
        while (frontier.isNotEmpty()) {
            current = frontier.poll()
            if (terminationCondition(current.first)) {
                success = true
                break
            } else {
                neighborIterator(current.first).filter { neighborFilter.filter(current.first, it) }.forEach { neighbor ->
                    val cost = costSoFar[current.first]!!.plus(edgeCost(current.first, neighbor))
                    if (!costSoFar.containsKey(neighbor) || cost < costSoFar[neighbor]!!) {
                        costSoFar[neighbor] = cost
                        frontier.add(neighbor to cost)
                    }
                }
            }
        }

        return if (success) costSoFar[current!!.first]!! else Int.MAX_VALUE
    }

    internal data class PathInfo<E>(val vertex: E, val list: List<E>, val costSoFar: Int)

    fun dijkstraShortestPath(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        terminationCondition: (VertexType) -> Boolean
    ): Pair<List<VertexType>, Int> {
        var success = false
        val frontier = PriorityQueue(compareBy<PathInfo<VertexType>> { it.costSoFar })
        val costSoFar = mutableMapOf<VertexType, Int>()
        costSoFar[start] = 0
        frontier.add(PathInfo(start, listOf(start), 0))
        var current: PathInfo<VertexType>? = null
        while (frontier.isNotEmpty()) {
            current = frontier.poll()
            if (terminationCondition(current.vertex)) {
                success = true
                break
            } else {
                neighborIterator(current.vertex).filter { neighborFilter.filter(current.vertex, it) }.forEach { neighbor ->
                    val cost = costSoFar[current.vertex]!!.plus(edgeCost(current.vertex, neighbor))
                    if (!costSoFar.containsKey(neighbor) || cost < costSoFar[neighbor]!!) {
                        costSoFar[neighbor] = cost
                        frontier.add(PathInfo(neighbor, current.list.plus(neighbor), cost))
                    }
                }
            }
        }

        return if (success) current!!.list to costSoFar[current.vertex]!!  else listOf<VertexType>() to Int.MAX_VALUE
    }

    fun dijkstraShortestPathAllVertices(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        terminationCondition: (VertexType) -> Boolean
    ): Pair<List<VertexType>, Int> {
        var success = false
        val frontier = PriorityQueue(compareBy<PathInfo<VertexType>> { it.costSoFar })
        val predecessors = mutableMapOf<VertexType, Set<VertexType>>()
        val costSoFar = mutableMapOf<VertexType, Int>()
        costSoFar[start] = 0
        frontier.add(PathInfo(start, listOf(start), 0))
        var current: PathInfo<VertexType>? = null
        while (frontier.isNotEmpty()) {
            current = frontier.poll()
            if (terminationCondition(current.vertex)) {
                success = true
                break
            } else {
                neighborIterator(current.vertex).filter { neighborFilter.filter(current.vertex, it) }.forEach { neighbor ->
                    val cost = costSoFar[current.vertex]!!.plus(edgeCost(current.vertex, neighbor))
                    if (!costSoFar.containsKey(neighbor) || cost <= costSoFar[neighbor]!!) {
                        costSoFar[neighbor] = cost
                        frontier.add(PathInfo(neighbor, current.list.plus(neighbor), cost))
                        predecessors[neighbor] = predecessors.getOrDefault(neighbor, setOf()).plus(current.vertex)
                    }
                }
            }
        }

        val set = mutableSetOf<VertexType>()
        val toProcess = mutableListOf<VertexType>()
        toProcess.add(current!!.vertex)
        while (toProcess.isNotEmpty()) {
            val processing = toProcess.removeFirst()
            set.add(processing)
            predecessors[processing]?.let {
                toProcess.addAll(it)
            }
        }

        return if (success) set.toList() to costSoFar[current.vertex]!!  else listOf<VertexType>() to Int.MAX_VALUE
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
                    potentialPath.addFirst(it to current.second.plus(current.first))
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