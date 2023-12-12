package util

import days.Day
import org.joda.time.DateTime
import org.reflections.Reflections
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@ExperimentalTime
object Runner {

    private lateinit var reflections: Reflections

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            val (year, day) =
                if (args.size == 1 && "today" == args[0]) {
                    listOf(DateTime.now().year.toString(), DateTime.now().dayOfMonth)
                } else {
                    if (args.size != 2) {
                        printError("Must specify year and day")
                    }
                    listOf(args[0].toInt(), args[1].toInt())
                }

            reflections = Reflections("days/aoc$year")

            val allDayClasses = getAllDayClasses()
            val dayClass = allDayClasses?.find { dayNumber(it.simpleName) == day }
            if (dayClass != null) {
                printDay(dayClass)
            }
            else {
                printError("Days.Day $day not found")
            }
        }
        else {
            reflections = Reflections("days")
            val allDayClasses = getAllDayClasses()
            if (allDayClasses != null) {
                allDayClasses.sortedBy { dayNumber(it.simpleName) }.forEach { printDay(it) }
            }
            else {
                printError("Couldn't find day classes - make sure you're in the right directory and try building again")
            }
        }
    }

    private fun getAllDayClasses(): MutableSet<Class<out Day>>? {
        return reflections.getSubTypesOf(Day::class.java)
    }

    private fun printDay(dayClass: Class<out Day>) {
        println("\n=== DAY ${dayNumber(dayClass.simpleName)} ===")
        val day = dayClass.constructors[0].newInstance() as Day

        val partOne = measureTimedValue { day.partOne() }
        val partTwo = measureTimedValue { day.partTwo() }
        printParts(partOne, partTwo)
    }

    private fun printParts(partOne: TimedValue<Any>, partTwo: TimedValue<Any>) {
        val padding = max(partOne.value.toString().length, partTwo.value.toString().length) + 14        // 14 is 8 (length of 'Part 1: ') + 6 more
        println("Part 1: ${partOne.value}".padEnd(padding, ' ') + "(${partOne.duration})")
        println("Part 2: ${partTwo.value}".padEnd(padding, ' ') + "(${partTwo.duration})")
    }

    private fun printError(message: String) {
        System.err.println("\n=== ERROR ===\n$message")
    }

    private fun dayNumber(dayClassName: String) = dayClassName.replace("Day", "").toInt()
}
