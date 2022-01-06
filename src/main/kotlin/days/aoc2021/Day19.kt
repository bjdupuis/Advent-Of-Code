package days.aoc2021

import days.Day
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.*
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.sum
import kotlin.math.absoluteValue


class Day19 : Day(2021, 19) {
    override fun partOne(): Any {
        //return countDistinctBeacons(orientScanners(inputList))
        return 0
    }

    override fun partTwo(): Any {
        return calculateMaximumDistanceBetweenScanners(orientScanners(inputList))
    }

    fun calculateMaximumDistanceBetweenScanners(scanners: List<Scanner>): Int {
        var max = 0
        for (i in 0 until scanners.lastIndex) {
            for (j in i+1..scanners.lastIndex) {
                max = maxOf(max, (scanners[i].offset!! - scanners[j].offset!!).map { it.absoluteValue }.sum())
            }
        }

        return max
    }

    fun countDistinctBeacons(scanners: List<Scanner>): Int {
        return scanners.flatMap { it.rotatedAndOffset()!! }.distinct().count()
    }

    fun orientScanners(inputLines: List<String>): List<Scanner> {
        var scanners = mutableListOf<Scanner>()
        inputLines.filter { !it.startsWith("---") }
            .fold(mutableListOf<D1Array<Int>>()) { list, line  ->
                if (line.isBlank() ) {
                    scanners.add(Scanner(list))
                    mutableListOf()
                } else {
                    Regex("(-?)(\\d+),(-?)(\\d+),(-?)(\\d+)").matchEntire(line.trim())?.destructured?.let { (xsign, x, ysign, y, zsign, z) ->
                        list.add(mk.ndarray(mk[
                            if (xsign == "-") x.toInt().unaryMinus() else x.toInt(),
                            if (ysign == "-") y.toInt().unaryMinus() else y.toInt(),
                            if (zsign == "-") z.toInt().unaryMinus() else z.toInt()
                        ]))
                    }
                    list
                }
            }

        scanners[0].naturalRotation = rotationList[0]
        scanners[0].offset = mk.ndarray(mk[0,0,0])

        do {
            scanners.filter { it.naturalRotation != null }.forEach { scanner1 ->
                scanners.filter { it.naturalRotation == null }.forEach { scanner2 ->
                    run rotations@{
                        rotations().forEach { rotation ->
                            val rotatedBeacons = scanner2.rotatedBy(rotation)
                            val scanner1Beacons = scanner1.rotatedAndOffset() ?: scanner1.beaconsDetected
                            scanner1Beacons.forEach { beaconFromScanner1 ->
                                rotatedBeacons.forEach { rotatedBeacon ->
                                    // assume these are the same beacon. Calculate the delta between
                                    // them and then apply that delta to all of scanner2's beacons
                                    val delta = rotatedBeacon - beaconFromScanner1
                                    val offsetBeacons = rotatedBeacons.map { rotated ->
                                        rotated - delta
                                    }
                                    val matchingBeacons =
                                        offsetBeacons.filter { scanner1Beacons.contains(it) }
                                    if (matchingBeacons.size >= 12) {
                                        scanner2.naturalRotation = rotation
                                        scanner2.offset = delta
                                        return@rotations
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } while (scanners.count { it.naturalRotation == null } != 0)

        return scanners
    }

    class Scanner(val beaconsDetected: List<D1Array<Int>>) {
        var naturalRotation: D2Array<Int>? = null
        var offset: NDArray<Int,D1>? = null

        fun rotatedAndOffset() = if (naturalRotation == null || offset == null) null else rotatedBy(naturalRotation!!).map { it - offset!! }

        fun rotatedBy(rotation: D2Array<Int>): List<D1Array<Int>> = beaconsDetected.map { beacon ->
            mk.ndarray(mk[
                rotation[0][0] * beacon[0] + rotation[0][1] * beacon[1] + rotation[0][2] * beacon[2],
                rotation[1][0] * beacon[0] + rotation[1][1] * beacon[1] + rotation[1][2] * beacon[2],
                rotation[2][0] * beacon[0] + rotation[2][1] * beacon[1] + rotation[2][2] * beacon[2]
            ])
        }
    }

    private fun rotations() = sequence {
        rotationList.forEach { yield(it) }
    }

    private val identityX = mk[1,0,0]
    private val identityY = mk[0,1,0]
    private val identityZ = mk[0,0,1]
    private val rotationX = mk[-1,0,0]
    private val rotationY = mk[0,-1,0]
    private val rotationZ = mk[0,0,-1]
    private val rotationList = listOf(
        mk.ndarray(mk[identityX, identityY, identityZ]),
        mk.ndarray(mk[identityX, rotationZ, identityY]),
        mk.ndarray(mk[identityX, rotationY, rotationZ]),
        mk.ndarray(mk[identityX, identityZ, rotationY]),

        mk.ndarray(mk[rotationX, rotationY, identityZ]),
        mk.ndarray(mk[rotationX, rotationZ, rotationY]),
        mk.ndarray(mk[rotationX, identityY, rotationZ]),
        mk.ndarray(mk[rotationX, identityZ, identityY]),

        mk.ndarray(mk[identityZ, rotationX, rotationY]),
        mk.ndarray(mk[identityY, rotationX, identityZ]),
        mk.ndarray(mk[rotationZ, rotationX, identityY]),
        mk.ndarray(mk[rotationY, rotationX, rotationZ]),

        mk.ndarray(mk[rotationZ, identityX, rotationY]),
        mk.ndarray(mk[identityY, identityX, rotationZ]),
        mk.ndarray(mk[identityZ, identityX, identityY]),
        mk.ndarray(mk[rotationY, identityX, identityZ]),

        mk.ndarray(mk[rotationY, rotationZ, identityX]),
        mk.ndarray(mk[identityZ, rotationY, identityX]),
        mk.ndarray(mk[identityY, identityZ, identityX]),
        mk.ndarray(mk[rotationZ, identityY, identityX]),

        mk.ndarray(mk[identityZ, identityY, rotationX]),
        mk.ndarray(mk[rotationY, identityZ, rotationX]),
        mk.ndarray(mk[rotationZ, rotationY, rotationX]),
        mk.ndarray(mk[identityY, rotationZ, rotationX])
    )

}
