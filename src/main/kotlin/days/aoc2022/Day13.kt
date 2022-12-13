package days.aoc2022

import days.Day

class Day13 : Day(2022,13) {
    override fun partOne(): Any {
        return calculateSumOfCountOfPairsInCorrectOrder(inputList)
    }

    override fun partTwo(): Any {
        return calculateDecoderKey(inputList)
    }

    fun calculateSumOfCountOfPairsInCorrectOrder(input: List<String>): Int {
        return input.filter { it.isNotEmpty() }.windowed(2, 2).mapIndexed { index, packets ->
            val left = parsePacket(packets.first())
            val right = parsePacket(packets.last())

            if (left <= right) index + 1 else 0
        }.sum()
    }

    fun calculateDecoderKey(input: List<String>): Int {
        val packets = input.filter { it.isNotEmpty() }.map { parsePacket(it) }
        return packets
            .plus(parsePacket("[[2]]"))
            .plus(parsePacket("[[6]]")).sorted().mapIndexed { index, packet ->
                if (packet.name == "[[2]]" || packet.name == "[[6]]") {
                    index + 1
                } else {
                    1
                }
            }.reduce { acc, i -> acc * i }
    }

    private fun parsePacket(line: String): Packet {
        val packet = PacketList(name = line)
        var current: PacketList? = packet
        var number = ""

        line.drop(1).forEach { c ->
            when (c) {
                '[' -> {
                    val newList = PacketList(name = null, parent = current)
                    current?.children?.add(newList)
                    current = newList
                }
                ']' -> {
                    if (number.isNotEmpty()) {
                        current?.children?.add(PacketInteger(number.toInt()))
                        number = ""
                    }
                    current = current?.parent
                }
                ',' -> {
                    if (number.isNotEmpty()) {
                        current?.children?.add(PacketInteger(number.toInt()))
                        number = ""
                    }
                }
                in '0'..'9' -> {
                    number += c
                }
            }
        }


        return packet
    }

    open class Packet(val name: String? = null) : Comparable<Packet> {
        override fun compareTo(other: Packet): Int {
            if (this is PacketList && other is PacketList) {
                for (i in children.indices) {
                    if (i in other.children.indices) {
                        val result = children[i].compareTo(other.children[i])
                        if (result != 0) {
                            return result
                        }
                    }
                }

                return children.lastIndex.compareTo(other.children.lastIndex)
            } else if (this is PacketInteger && other is PacketInteger) {
                return value.compareTo(other.value)
            } else {
                val temp = PacketList(null)
                return if (this is PacketList) {
                    temp.children.add(other)
                    compareTo(temp)
                } else {
                    temp.children.add(this)
                    temp.compareTo(other)
                }
            }
        }
    }

    class PacketList(name: String?, val parent: PacketList? = null) : Packet(name) {
        val children = mutableListOf<Packet>()
    }

    class PacketInteger(val value: Int) : Packet()
}