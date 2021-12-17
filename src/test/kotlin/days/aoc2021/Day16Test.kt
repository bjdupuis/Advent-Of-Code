package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day16Test {
    private val day = Day16()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.convertHexToBinary("D2FE28"), `is`("110100101111111000101000"))
        MatcherAssert.assertThat(day.convertHexToBinary("38006F45291200"), `is`("00111000000000000110111101000101001010010001001000000000"))
        MatcherAssert.assertThat(day.convertHexToBinary("EE00D40C823060"), `is`("11101110000000001101010000001100100000100011000001100000"))

        MatcherAssert.assertThat(day.convertBinaryToInt("000000000011011"), `is`(27))
        MatcherAssert.assertThat(day.convertBinaryToInt("011111100101"), `is`(2021))

        MatcherAssert.assertThat(day.calculateSumOfPacketVersions("8A004A801A8002F478"), `is`(16))
        MatcherAssert.assertThat(day.calculateSumOfPacketVersions("620080001611562C8802118E34"), `is`(12))
        MatcherAssert.assertThat(day.calculateSumOfPacketVersions("C0015000016115A2E0802F182340"), `is`(23))
        MatcherAssert.assertThat(day.calculateSumOfPacketVersions("A0016C880162017C3686B18A3D4780"), `is`(31))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculateValueForPacket("C200B40A82"), `is`(3))
        MatcherAssert.assertThat(day.calculateValueForPacket("04005AC33890"), `is`(54))
        MatcherAssert.assertThat(day.calculateValueForPacket("880086C3E88112"), `is`(7))
        MatcherAssert.assertThat(day.calculateValueForPacket("CE00C43D881120"), `is`(9))
        MatcherAssert.assertThat(day.calculateValueForPacket("D8005AC2A8F0"), `is`(1))
        MatcherAssert.assertThat(day.calculateValueForPacket("F600BC2D8F"), `is`(0))
        MatcherAssert.assertThat(day.calculateValueForPacket("9C005AC2F8F0"), `is`(0))
        MatcherAssert.assertThat(day.calculateValueForPacket("9C0141080250320F1802104A08"), `is`(1))
    }
}