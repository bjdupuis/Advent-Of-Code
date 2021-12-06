package days.aoc2020

import days.Day

class Day14: Day(2020, 14) {
    override fun partOne(): Any {
        val memory = mutableMapOf<Long,Long>()
        var bitMaskClear = 0L
        var bitMaskSet = 0L

        inputList.forEach { instruction ->
            when {
                instruction.startsWith("mask") -> {
                    Regex("mask = ([01X]+)").matchEntire(instruction)?.destructured?.let { (mask) ->
                        bitMaskClear = 0L
                        bitMaskSet = 0L
                        var current = 1L
                        mask.reversed().forEach {
                            when(it) {
                                '1' -> bitMaskSet += current
                                '0' -> bitMaskClear += current
                            }
                            current = current shl 1
                        }
                        bitMaskClear = bitMaskClear.inv()
                    }
                }
                instruction.startsWith("mem") -> {
                    Regex("mem\\[(\\d+)\\] = (\\d+)").matchEntire(instruction)?.destructured?.let { (address, value) ->
                        value.toLong().apply {
                            memory[address.toLong()] = (this or bitMaskSet) and bitMaskClear
                        }
                    }
                }
            }
        }

        return memory.filterNot { it.value == 0L }?.map { it.value }?.sum()
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartTwo(list: List<String>): Long {
        val memory = mutableMapOf<Long,Long>()
        var currentMask = ""

        list.forEach { instruction ->
            when {
                instruction.startsWith("mask") -> {
                    Regex("mask = ([01X]+)").matchEntire(instruction)?.destructured?.let { (mask) ->
                        currentMask = mask
                    }
                }
                instruction.startsWith("mem") -> {
                    Regex("mem\\[(\\d+)\\] = (\\d+)").matchEntire(instruction)?.destructured?.let { (address, value) ->
                        value.toLong().apply {
                            addressSequence(currentMask, address.toLong()).forEach {
                                memory[it] = value.toLong()
                            }
                        }
                    }
                }
            }
        }

        return memory.filterNot { it.value == 0L }?.map { it.value }?.sum()
    }

    private fun addressSequence(mask: String, address: Long) = sequence {
        var bitMaskSet = 0L
        var bitMaskClear = 0L
        var current = 1L
        val floaters = mutableListOf<Long>()
        mask.reversed().forEach {
            when(it) {
                '1' -> bitMaskSet += current
                'X' -> {
                    bitMaskClear += current
                    floaters.add(current)
                }
            }
            current = current shl 1
        }
        bitMaskClear = bitMaskClear.inv()

        val masksAppliedToAddress = (address or bitMaskSet) and bitMaskClear

        permute(masksAppliedToAddress, floaters).forEach { address ->
            yield(address)
        }
    }

    private fun permute(currentMask: Long, floaters: List<Long>): List<Long> {
        if (floaters.size == 1) {
            return listOf(currentMask, currentMask + floaters[0])
        }

        val permutations = mutableListOf<Long>()
        val first = floaters.first()
        permute(currentMask, floaters.drop(1)).forEach { l ->
            permutations.add(l)
            permutations.add(first + l)
        }

        return permutations
    }


}