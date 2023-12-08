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

