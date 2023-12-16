package util

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
}

