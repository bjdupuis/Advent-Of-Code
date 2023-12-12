package days.aoc2023

import days.Day

class Day12 : Day(2023, 12) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        // brute force it
        return input.sumOf { line ->
            val (record, contiguousSpringsList) = line.split(" ").let {
                it.first() to it.last().split(",").map { it.toInt() }
            }

            constructSpringPermutations(record).count {
                isValidCondition(
                    it,
                    contiguousSpringsList
                )
            }
        }
    }

    private fun constructSpringPermutations(record: String): List<String> {
        val permutations = mutableListOf<String>()
        val deque = ArrayDeque<String>()
        deque.add(record)
        while (deque.isNotEmpty()) {
            val current = deque.removeFirst()
            if (current.indexOfFirst { it == '?' } >= 0) {
                deque.add(current.replaceFirst('?','.'))
                deque.add(current.replaceFirst('?','#'))
            } else {
                permutations.add(current)
            }
        }

        return permutations
    }

    fun isValidCondition(springConditions: String, contiguousSprings: List<Int>): Boolean {
        val pattern = "\\.*" + contiguousSprings.joinToString("\\.+") { springCount ->
            "#".repeat(
                springCount
            )
        } + "\\.*"
        return Regex(pattern).matches(springConditions)
    }

    fun calculatePartTwo(input: List<String>): Long {
        return input.sumOf { line ->
            val (record, contiguousSpringsList) = line.split(" ").let {
                it.first().plus("?").repeat(5).dropLast(1) to it.last().plus(",").repeat(5).dropLast(1).split(",").map { it.toInt() }
            }

            // can't brute force this :|
            val count = countValidRemaining(record, contiguousSpringsList)
            //println("Found $count valid configs for $record")
            count
        }
    }

    val memoize = mutableMapOf<Pair<String, List<Int>>, Long>()
    private fun countValidRemaining(record: String, contiguousSprings: List<Int>): Long {
        if (contiguousSprings.isEmpty()) {
            // if there are any other broken springs in the (optional) remainder of the record
            // it's not a valid record
            return if (record.contains('#')) 0 else 1
        } else if (record.isEmpty()) {
            // empty record but non-empty springs list... we're missing springs
            return 0
        }

        var count = 0L
        return memoize.getOrPut(Pair(record, contiguousSprings)) {
            if (record.first() == '#' || record.first() == '?') {
                val contiguousLength = contiguousSprings.first()
                // count as though the ? is actually a #... we've (potentially) found a group of
                // contiguous broken springs. Verify this group is possible and count the
                // remaining groups. Possible groups mean that:
                //
                // * the remaining record can even hold the group of springs
                // * the current record doesn't have any working springs in the contiguous length
                // * it's the end of the record or there's either a working spring after it
                if (record.length >= contiguousLength &&
                    !record.take(contiguousLength).contains(".") &&
                    (record.drop(contiguousLength).isEmpty() || record.drop(contiguousLength)
                        .first() in "?.")
                ) {
                    // this contiguous group is okay... count the remainder after dropping the contiguous group
                    // PLUS the following one since we have to assume it's a '.' for this config to work
                    count += countValidRemaining(
                        record.drop(contiguousLength + 1),
                        contiguousSprings.drop(1)
                    )
                }
            }
            if (record.first() == '.' || record.first() == '?') {
                // count as though the ? is actually a .
                count += countValidRemaining(record.drop(1), contiguousSprings)
            }

            count
        }
    }
}