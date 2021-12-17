package days.aoc2021

import days.Day

class Day16 : Day(2021, 16) {
    override fun partOne(): Any {
        return calculateSumOfPacketVersions(inputString)
    }

    override fun partTwo(): Any {
        return calculateValueForPacket(inputString)
    }

    fun calculateSumOfPacketVersions(packet: String): Int {
        val binary = convertHexToBinary(packet.trim())
        val packet = Packet(binary)

        packet.parsePacketsForVersionSum(binary)
        return packet.versionSum
    }

    fun calculateValueForPacket(packet: String): Long {
        val binary = convertHexToBinary(packet.trim())
        val packet = Packet(binary)

        parsePacketForValue(packet)
        return packet.value
    }

    fun parsePacketForValue(packet: Packet): Packet {
        with (packet) {
            if (packetData.isEmpty() || convertBinaryToInt(packetData) == 0) {
                return this
            }

            // don't care about packet versions anymore... skip them
            packetData.drop(3).take(3).let { type ->
                packetData = packetData.drop(6)
                when (type) {
                    "100" -> { // literal
                        var valueString = ""
                        while (packetData.first() == '1') {
                            packetData = packetData.drop(1)
                            valueString += packetData.take(4)
                            packetData = packetData.drop(4)
                        }
                        // get the last one
                        packetData = packetData.drop(1)
                        valueString += packetData.take(4)
                        packetData = packetData.drop(4)

                        value = convertBinaryToLong(valueString)
                        println("Literal has value $value")
                        return this
                    }
                    else -> {
                        val subpackets = mutableListOf<Packet>()
                        when (packetData.first()) {
                            '1' -> { // number of subpackets is an 11-bit number
                                val numberOfSubpackets =
                                    convertBinaryToInt(packetData.drop(1).take(11))
                                println("  -- operator with $numberOfSubpackets subpackets")
                                packetData = packetData.drop(12)

                                repeat(numberOfSubpackets) {
                                    println("  -- parsing subpacket $it")
                                    val newPacket = parsePacketForValue(Packet(packetData))
                                    subpackets.add(newPacket)
                                    packetData = newPacket.packetData
                                }
                            }
                            '0' -> { // length of subpackets is a 15-bit number
                                val lengthOfSubpackets =
                                    convertBinaryToInt(packetData.drop(1).take(15))
                                println("  -- operator with $lengthOfSubpackets size of subpackets")
                                packetData = packetData.drop(16)

                                var subpacketsData = packetData.take(lengthOfSubpackets)
                                while (subpacketsData.isNotEmpty()) {
                                    val newPacket = parsePacketForValue(Packet(subpacketsData))
                                    subpackets.add(newPacket)
                                    subpacketsData = newPacket.packetData
                                }
                                packetData = packetData.drop(lengthOfSubpackets)
                            }
                        }
                        when (type) {
                            "000" -> { // sum
                                print("Sum: ")
                                value = subpackets.fold(0L) { acc, packet ->
                                    print(" ${packet.value}")
                                    acc + packet.value
                                }
                            }
                            "001" -> { // product
                                print("Product: ")
                                value = subpackets.fold(1L) { acc, packet ->
                                    print(" ${packet.value}")
                                    acc * packet.value
                                }
                            }
                            "010" -> { // minimum
                                print("Minimum: ")
                                value = subpackets.reduce { acc, packet ->
                                    print("${acc.value} ${packet.value}")
                                    if (acc.value < packet.value) acc else packet
                                }.value
                            }
                            "011" -> { // maximum
                                print("Maximum: ")
                                value = subpackets.reduce { first, second ->
                                    print("$first $second")
                                    if (first.value > second.value) first else second
                                }.value
                            }
                            "101" -> { // greater than
                                print("> : ")
                                value =
                                    if (subpackets.removeFirst().value > subpackets.removeFirst().value)
                                        1
                                    else
                                        0
                            }
                            "110" -> { // less than
                                print("< : ")
                                value =
                                    if (subpackets.removeFirst().value < subpackets.removeFirst().value)
                                        1
                                    else
                                        0
                            }
                            "111" -> { // equal to
                                print("== : ")
                                value =
                                    if (subpackets.removeFirst().value == subpackets.removeFirst().value)
                                        1
                                    else
                                        0
                            }
                        }
                        println(" = $value")
                    }
                }
            }
        }

        return packet
    }

    inner class Packet(var packetData: String) {
        var versionSum = 0
        var value = 0L

        fun parsePacketsForVersionSum(current: String?, continueProcessing: Boolean = true): String? {
            var remaining: String? = current

            while (remaining?.isNotEmpty() == true) {
                if (convertBinaryToInt(remaining) == 0) {
                    return null
                }

                remaining.take(3).let { versionString ->
                    remaining = remaining?.drop(3)
                    versionSum += convertBinaryToInt(versionString)
                }

                remaining?.take(3).let { type ->
                    remaining = remaining?.drop(3)
                    when (type) {
                        "100" -> { // literal packet

                            // step through literal definition
                            while (remaining?.first() == '1') {
                                // not the last digit, skip the indicator and 4 bits
                                remaining = remaining?.drop(5)
                            }

                            // last digit. Skip the indicator and 4 bits
                            remaining = remaining?.drop(5)

                            if (!continueProcessing) {
                                return remaining
                            }
                        }
                        else -> { // operator packet
                            when (remaining?.first()) {
                                '1' -> { // number of subpackets is an 11-bit number
                                    val numberOfSubpackets =
                                        convertBinaryToInt(remaining?.drop(1)?.take(11))
                                    remaining = remaining?.drop(12)

                                    repeat(numberOfSubpackets) {
                                        remaining = parsePacketsForVersionSum(remaining, false)
                                    }

                                    if (!continueProcessing) {
                                        return remaining
                                    }
                                }
                                '0' -> { // length of subpackets is a 15-bit number
                                    val lengthOfSubpackets =
                                        convertBinaryToInt(remaining?.drop(1)?.take(15))
                                    remaining = remaining?.drop(16)

                                    parsePacketsForVersionSum(remaining?.take(lengthOfSubpackets))
                                    remaining = remaining?.drop(lengthOfSubpackets)
                                    if (!continueProcessing) {
                                        return remaining
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null
        }

    }

    fun convertBinaryToLong(binary: String?): Long {
        return binary?.reversed()?.foldIndexed(0L) { index, total, digit ->
            total + 1L.shl(index) * (digit - '0')
        } ?: throw IllegalArgumentException()
    }

    fun convertBinaryToInt(binary: String?): Int {
        return binary?.reversed()?.foldIndexed(0) { index, total, digit ->
            total + 1.shl(index) * (digit - '0')
        } ?: throw IllegalArgumentException()
    }

    fun convertHexToBinary(hex: String): String {
        return hex.map { hexDigit ->
            when (hexDigit) {
                '0' -> "0000"
                '1' -> "0001"
                '2' -> "0010"
                '3' -> "0011"
                '4' -> "0100"
                '5' -> "0101"
                '6' -> "0110"
                '7' -> "0111"
                '8' -> "1000"
                '9' -> "1001"
                'A' -> "1010"
                'B' -> "1011"
                'C' -> "1100"
                'D' -> "1101"
                'E' -> "1110"
                'F' -> "1111"
                else -> throw IllegalArgumentException()
            }
        }.joinToString("")
    }
}