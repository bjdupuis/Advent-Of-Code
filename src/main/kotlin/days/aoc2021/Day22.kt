package days.aoc2021

import days.Day
import org.jetbrains.kotlinx.multik.api.d3array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.count

class Day22 : Day(2021, 22) {
    override fun partOne(): Any {
        val reactor = Reactor(-50..50, -50..50, -50..50)
        return calculateCubesTurnedOn(inputList, reactor)
    }

    override fun partTwo(): Any {
        val reactor = InfiniteReactor()
        return calculateCubesTurnedOnInfinite(inputList, reactor)
    }

    fun calculateCubesTurnedOn(inputList: List<String>, reactor: Reactor): Int {
        inputList.forEach { line ->
            Regex("(on|off) x=(-?)(\\d+)..(-?)(\\d+),y=(-?)(\\d+)..(-?)(\\d+),z=(-?)(\\d+)..(-?)(\\d+)").
                matchEntire(line.trim())?.groupValues?.let { groups ->
                if (groups.size != 14) {
                    throw IllegalStateException()
                }

                val x1 = with(groups[3].toInt()) {  if ("-" == groups[2]) this.unaryMinus() else this }
                val x2 = with(groups[5].toInt()) {  if ("-" == groups[4]) this.unaryMinus() else this }
                val y1 = with(groups[7].toInt()) {  if ("-" == groups[6]) this.unaryMinus() else this }
                val y2 = with(groups[9].toInt()) {  if ("-" == groups[8]) this.unaryMinus() else this }
                val z1 = with(groups[11].toInt()) {  if ("-" == groups[10]) this.unaryMinus() else this }
                val z2 = with(groups[13].toInt()) {  if ("-" == groups[12]) this.unaryMinus() else this }
                val xRange = if (x1 < x2) x1..x2 else x1 downTo x2
                val yRange = if (y1 < y2) y1..y2 else y1 downTo y2
                val zRange = if (z1 < z2) z1..z2 else z1 downTo z2

                reactor.switchBulbs(groups[1] == "on", xRange, yRange, zRange)
            }

        }

        return reactor.cubes.count { it == 1 }
    }

    class Reactor(private val xRange: IntRange, private val yRange: IntRange, private val zRange: IntRange) {
        val cubes = mk.d3array((xRange.last - xRange.first) + 1, (yRange.last - yRange.first) + 1, (zRange.last - zRange.first) + 1) { 0 }

        fun switchBulbs(
            onOff: Boolean,
            x: IntProgression,
            y: IntProgression,
            z: IntProgression
        ) {
            x.intersect(xRange).map { it - xRange.first }.forEach { xIndex ->
                y.intersect(yRange).map { it - yRange.first }.forEach { yIndex ->
                    z.intersect(zRange).map { it - zRange.first }.forEach { zIndex ->
                        cubes[xIndex, yIndex, zIndex] = if (onOff) 1 else 0
                    }
                }
            }
        }
    }

    fun calculateCubesTurnedOnInfinite(inputList: List<String>, reactor: InfiniteReactor): Long {
        inputList.forEach { line ->
            Regex("(on|off) x=(-?)(\\d+)..(-?)(\\d+),y=(-?)(\\d+)..(-?)(\\d+),z=(-?)(\\d+)..(-?)(\\d+)").
            matchEntire(line.trim())?.groupValues?.let { groups ->
                if (groups.size != 14) {
                    throw IllegalStateException()
                }

                val x1 = with(groups[3].toInt()) {  if ("-" == groups[2]) this.unaryMinus() else this }
                val x2 = with(groups[5].toInt()) {  if ("-" == groups[4]) this.unaryMinus() else this }
                val y1 = with(groups[7].toInt()) {  if ("-" == groups[6]) this.unaryMinus() else this }
                val y2 = with(groups[9].toInt()) {  if ("-" == groups[8]) this.unaryMinus() else this }
                val z1 = with(groups[11].toInt()) {  if ("-" == groups[10]) this.unaryMinus() else this }
                val z2 = with(groups[13].toInt()) {  if ("-" == groups[12]) this.unaryMinus() else this }
                val xRange = if (x1 < x2) x1..x2 else x2..x1
                val yRange = if (y1 < y2) y1..y2 else y2..y1
                val zRange = if (z1 < z2) z1..z2 else z2..z1

                reactor.cuboids.add(Pair(Triple(xRange, yRange, zRange), groups[1] == "on"))
            }

        }

        return reactor.totalOn()
    }


    class InfiniteReactor {
        val cuboids = mutableListOf<Pair<Triple<IntRange, IntRange, IntRange>,Boolean>>()

        fun totalOn(): Long {
            val positiveCuboids = mutableListOf<Triple<IntRange, IntRange, IntRange>>()

            for (cuboid in cuboids) {
                if (cuboid.second) { // it's an "on" cuboid
                    if (positiveCuboids.isEmpty()) {
                        positiveCuboids.add(cuboid.first)
                    } else {
                        val newFragments = positiveCuboids.fold(listOf(cuboid.first)) { acc, existing ->
                            val toRemove = mutableListOf<Triple<IntRange, IntRange, IntRange>>()
                            val toAdd = mutableListOf<Triple<IntRange, IntRange, IntRange>>()

                            acc.forEach { current ->
                                if (current intersects existing) {
                                    toRemove.add(current)
                                    toAdd.addAll(current removeIntersection existing)
                                }
                            }

                            acc.minus(toRemove).plus(toAdd)
                        }

                        positiveCuboids.addAll(newFragments)
                    }
                } else { // it's an "off" cuboid
                    if (positiveCuboids.isEmpty()) {
                        // ignore... we start all off so if our list is empty the first one is turning nothing off
                    } else {
                        val toRemove = mutableListOf<Triple<IntRange, IntRange, IntRange>>()
                        val toAdd = mutableListOf<Triple<IntRange, IntRange, IntRange>>()
                        positiveCuboids.forEach {
                            if (it intersects cuboid.first) {
                                toRemove.add(it)
                                toAdd.addAll(it removeIntersection cuboid.first)
                            }
                        }

                        toRemove.forEach { positiveCuboids.remove(it) }
                        positiveCuboids.addAll(toAdd)
                    }
                }
            }

            return positiveCuboids.sumOf { it.volume() }
        }

        private val ClosedRange<Int>.min
            get() = minOf(start, endInclusive)

        private val ClosedRange<Int>.max
            get() = maxOf(start, endInclusive)

        private val ClosedRange<Int>.size
            get() = ((max - min) + 1).toLong()

        private fun Triple<IntRange, IntRange, IntRange>.volume(): Long {
            return first.size * second.size * third.size
        }

        //                                                           -----------
        //                                                          75         90
        //              ----------------------------------------
        //             -5                                      70
        //    ---------------------------------------
        //   -20                                    50
        private fun ClosedRange<Int>.overlapLength(other: ClosedRange<Int>): Long =
            ((minOf(endInclusive, other.endInclusive).toLong() - maxOf(start, other.start).toLong()) + 1)
                .coerceAtLeast(0L)

        private infix fun ClosedRange<Int>.intersect(other: ClosedRange<Int>): IntRange? =
            if (overlapLength(other) > 0) {
                maxOf(start, other.start)..minOf(endInclusive, other.endInclusive)
            } else {
                null
            }

        private infix fun Triple<IntRange, IntRange, IntRange>.intersect(other: Triple<IntRange, IntRange, IntRange>) : Triple<IntRange, IntRange, IntRange>? {
            val newFirst = first intersect other.first
            val newSecond = second intersect other.second
            val newThird = third intersect other.third
            return if (newFirst != null && newSecond != null && newThird != null)
                Triple(newFirst, newSecond, newThird)
            else
                null
        }

        private infix fun Triple<IntRange, IntRange, IntRange>.intersects(other: Triple<IntRange, IntRange, IntRange>) : Boolean {
            return (this intersect other) != null
        }

        private infix fun Triple<IntRange, IntRange, IntRange>.removeIntersection(other: Triple<IntRange, IntRange, IntRange>): List<Triple<IntRange, IntRange, IntRange>> {
            val intersection = this intersect other
            val subCuboids = mutableListOf<Triple<IntRange,IntRange,IntRange>>()
            if (intersection == null) {
                return listOf(this)
            } else if (intersection.volume() == volume()) { // if the intersection is our entire volume, we just don't exist any more
                return emptyList()
            }

            // our X axis will have the "endcaps" of the cuboid.
            if (first.first != intersection.first.first) {
                subCuboids.add(
                    Triple(first.first until intersection.first.first,
                        second,
                        third))
            }

            if (first.last != intersection.first.last) {
                subCuboids.add(
                    Triple(intersection.first.last + 1..first.last,
                        second,
                        third))
            }

            // the Y axis will be bounded by the "endcaps" of the X
            if (second.first != intersection.second.first) {
                subCuboids.add(
                    Triple(maxOf(first.first, intersection.first.first)..minOf(first.last, intersection.first.last),
                        second.first until intersection.second.first,
                        third))
            }

            if (second.last != intersection.second.last) {
                subCuboids.add(
                    Triple(maxOf(first.first, intersection.first.first)..minOf(first.last, intersection.first.last),
                        intersection.second.last + 1..second.last,
                        third))
            }

            // the Z axis fills in the final gaps
            if (third.first != intersection.third.first) {
                subCuboids.add(
                    Triple(maxOf(first.first, intersection.first.first)..minOf(first.last, intersection.first.last),
                        maxOf(second.first, intersection.second.first)..minOf(second.last, intersection.second.last),
                        third.first until intersection.third.first))
            }
            if (third.last != intersection.third.last) {
                subCuboids.add(
                    Triple(maxOf(first.first, intersection.first.first)..minOf(first.last, intersection.first.last),
                        maxOf(second.first, intersection.second.first)..minOf(second.last, intersection.second.last),
                        intersection.third.last + 1..third.last))
            }


            return subCuboids
        }

    }

}