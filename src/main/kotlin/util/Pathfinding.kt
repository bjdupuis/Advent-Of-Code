package util

import java.nio.file.Path
import java.util.PriorityQueue
import kotlin.math.max

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

    internal data class PathInfo<E>(val vertex: E, val list: List<E>, val costSoFar: Int)

    fun findAllPathsDfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        maxCost: Int,
        terminationCondition: (VertexType) -> Boolean
    ): List<List<VertexType>> {
        val completedPaths = mutableListOf<List<VertexType>>()
        val potentialPath = ArrayDeque<PathInfo<VertexType>>()
        potentialPath.addFirst(PathInfo(start, listOf(), 0))

        while (potentialPath.isNotEmpty()) {
            val current = potentialPath.removeFirst()
            if (terminationCondition(current.vertex)) {
                completedPaths.add(current.list.plus(current.vertex))
            } else {
                neighborIterator(current.vertex).filter { neighborFilter.filter(current.vertex, it) }
                    .filter { !current.list.contains(it) }.forEach {
                        val cost = edgeCost(current.vertex, it)
                        if (current.costSoFar + cost <= maxCost) {
                            potentialPath.addFirst(
                                PathInfo(
                                    it,
                                    current.list.plus(current.vertex),
                                    current.costSoFar + cost
                                )
                            )
                        }
                    }
            }
        }

        return completedPaths
    }

    fun findAllPathsDfsRecursive(
        current: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        maxCost: Int,
        terminationCondition: (VertexType) -> Boolean
    ): List<List<VertexType>> {
        return recurseDfs(
            PathInfo(current, listOf(current), 0),
            neighborIterator,
            neighborFilter,
            edgeCost,
            maxCost,
            terminationCondition,
            listOf()
        )
    }

    private fun recurseDfs(
        current: PathInfo<VertexType>,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        maxCost: Int,
        terminationCondition: (VertexType) -> Boolean,
        completedPaths: List<List<VertexType>>
    ): List<List<VertexType>> {
        if (terminationCondition(current.vertex)) {
            return completedPaths.plus(listOf(current.list.plus(current.vertex)))
        } else {
            val neighbors = neighborIterator(current.vertex).filter { neighborFilter.filter(current.vertex, it) }.filter { !current.list.contains(it) }
            if (neighbors.isEmpty()) {
                return completedPaths
            } else {
                return neighbors.map {
                    val cost = edgeCost(current.vertex, it)
                    return if (current.costSoFar + cost <= maxCost) {
                        recurseDfs(
                            PathInfo(it, current.list.plus(it), current.costSoFar + cost),
                            neighborIterator,
                            neighborFilter,
                            edgeCost,
                            maxCost,
                            terminationCondition,
                            completedPaths
                        )
                    } else {
                        completedPaths
                    }
                }
            }
        }
    }

    fun findAllPathsBfs(
        start: VertexType,
        neighborIterator: (VertexType) -> List<VertexType>,
        neighborFilter: NeighborFilter<VertexType>,
        edgeCost: (VertexType, VertexType) -> Int,
        maxCost: Int,
        terminationCondition: (VertexType) -> Boolean
    ): List<List<VertexType>> {
        val completedPaths = mutableListOf<List<VertexType>>()
        val potentialPath = ArrayDeque<PathInfo<VertexType>>()
        potentialPath.addFirst(PathInfo(start, listOf(), 0))

        while (potentialPath.isNotEmpty()) {
            val current = potentialPath.removeLast()
            if (terminationCondition(current.vertex)) {
                completedPaths.add(current.list.plus(current.vertex))
            } else {
                neighborIterator(current.vertex).filter { neighborFilter.filter(current.vertex, it) }
                    .filter { !current.list.contains(it) }.forEach {
                        val cost = edgeCost(current.vertex, it)
                        if (current.costSoFar + cost <= maxCost) {
                            potentialPath.addFirst(
                                PathInfo(
                                    it,
                                    current.list.plus(current.vertex),
                                    current.costSoFar + cost
                                )
                            )
                        }
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