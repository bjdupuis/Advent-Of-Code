package util

fun Collection<Int>.lcm(): Long {
    val primes = generateSequence(2 to generateSequence(3) {it + 2}) { pair ->
        val currSeq = pair.second.iterator()
        val nextPrime = currSeq.next()
        nextPrime to currSeq.asSequence().filter { it % nextPrime != 0}
    }.iterator()
    var remaining = this.toMutableList()
    val primesFound = mutableListOf<Int>()
    var currentDivisor = primes.next()

    while (remaining.isNotEmpty()) {
        if (remaining.any { it % currentDivisor.first == 0 } ) {
            remaining = remaining.map {
                if (it % currentDivisor.first == 0) {
                    it / currentDivisor.first
                } else {
                    it
                }
            }.toMutableList()
            remaining.removeIf { it == 1 }
            primesFound.add(currentDivisor.first)
        } else {
            currentDivisor = primes.next()
        }
    }

    return primesFound.fold(1) { acc, i -> acc  * i}
}

infix fun Int.downUntil(to: Int): IntProgression {
    if (to >= Int.MAX_VALUE) return IntRange.EMPTY
    return this downTo (to + 1)
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun Int.isOdd(): Boolean = this % 2 == 1
fun Int.isEven(): Boolean = this % 2 == 0

fun <E> MutableList<E>.swapElements(first: E, second: E) {
    val index = indexOf(first)
    val value = this[indexOf(second)]
    this[indexOf(second)] = this[index]
    this[index] = value
}

fun <E> Iterable<E>.combinations(minimum: Int, maximum: Int = minimum): Sequence<List<E>> =
    sequence {
        length@ for (length in minimum..maximum) {
            val pool = this@combinations as? List<E> ?: toList()
            val n = pool.size
            if (length > n) return@sequence
            val indices = IntArray(length) { it }
            while (true) {
                yield(indices.map { pool[it] })
                var i = length
                do {
                    i--
                    if (i == -1) continue@length
                } while (indices[i] == i + n - length)
                indices[i]++
                for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
            }
        }
    }

fun <E> Iterable<E>.permutations(minimum: Int? = null, maximum: Int? = minimum): Sequence<List<E>> =
    sequence {
        length@ for (length in (minimum ?: this@permutations.count())..(maximum ?: this@permutations.count())) {
            val pool = this@permutations as? List<E> ?: toList()
            val n = pool.size
            if (length > n) return@sequence
            val indices = IntArray(n) { it }
            val cycles = IntArray(length) { n - it }
            yield(List(length) { pool[indices[it]] })
            if (n == 0) continue@length
            cyc@ while (true) {
                for (i in length - 1 downTo 0) {
                    cycles[i]--
                    if (cycles[i] == 0) {
                        val temp = indices[i]
                        for (j in i until n - 1) indices[j] = indices[j + 1]
                        indices[n - 1] = temp
                        cycles[i] = n - i
                    } else {
                        val j = n - cycles[i]
                        indices[i] = indices[j].also { indices[j] = indices[i] }
                        yield(List(length) { pool[indices[it]] })
                        continue@cyc
                    }
                }
                continue@length
            }
        }
    }