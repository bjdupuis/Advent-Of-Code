package util

// Bron-Kerbosch algorithm for maximal cliques. 
class Cliques<E> {
    fun <E : Any> findMaximalClique(connections: Map<E, Set<E>>, vertices: MutableSet<E>): Set<E> {
        return findMaximalCliques(connections, emptySet(), vertices, mutableSetOf()).maxBy { it.size }
    }

    fun <E : Any> findMaximalCliques(connections: Map<E, Set<E>>, r: Set<E>, p: MutableSet<E>, x: MutableSet<E>): List<Set<E>> {
        if (p.isEmpty() && x.isEmpty()) {
            return listOf(r)
        }
        val vertices = p.toList()
        val returnValue = mutableListOf<Set<E>>()
        vertices.forEach { v ->
            returnValue.addAll(
                findMaximalCliques(connections, r.plus(v), p.intersect(connections[v]!!).toMutableSet(), x.intersect(connections[v]!!).toMutableSet())
            )
            p.remove(v)
            x.add(v)
        }

        return returnValue
    }
}