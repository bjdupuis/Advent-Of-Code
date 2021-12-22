package days.aoc2021

import days.Day
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.minus


class Day19 : Day(2021, 19) {
    override fun partOne(): Any {
        return 0
    }

    override fun partTwo(): Any {
        return 0
    }

    fun countBeacons(inputLines: List<String>): Int {
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

        val beacons = mutableListOf<D1Array<Int>>()
        for (i in 0 until scanners.lastIndex) {
            for (j in i+1..scanners.lastIndex) {
                val scanner1 = scanners[i]
                val scanner2 = scanners[j]

                rotations().forEach { rotation ->
                    val matchingBeacons = scanner2.rotatedBy(rotation).filter { scanner1.vectors.contains(it) }
                    if (matchingBeacons.size >= 12) {
                        println("Found matching rotation ($rotation) for $i and $j")
                        beacons.addAll(matchingBeacons)
                    }
                }
            }
        }


        return beacons.size
    }

    class Scanner(beaconsDetected: List<D1Array<Int>>) {

        val vectors = mutableListOf<D1Array<Int>>()
        init {
            for (i in 0 until beaconsDetected.lastIndex) {
                for (j in i+1..beaconsDetected.lastIndex) {
                    val delta = beaconsDetected[i] - beaconsDetected[j]
                    vectors.add(delta)
                }
            }
        }

        fun rotatedBy(rotation: D2Array<Int>): List<D1Array<Int>> = vectors.map { beacon ->
            mk.ndarray(mk[
                rotation[0][0] * beacon[0] + rotation[0][1] * beacon[0] + rotation[0][2] * beacon[0],
                rotation[1][0] * beacon[1] + rotation[1][1] * beacon[1] + rotation[1][2] * beacon[1],
                rotation[2][0] * beacon[2] + rotation[2][1] * beacon[2] + rotation[2][2] * beacon[2]
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
