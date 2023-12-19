package util

fun Collection<Int>.lcm(): Long {
    val primes = generateSequence(2 to generateSequence(3) {it + 2}) {
        val currSeq = it.second.iterator()
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

inline fun Int.isOdd(): Boolean = this % 2 == 1
inline fun Int.isEven(): Boolean = this % 2 == 0
