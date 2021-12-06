package days.aoc2015

import days.Day

class Day11: Day(2015, 11) {
    override fun partOne(): Any {
        return findNextPassword(inputString)
    }

    fun validatePassword(password: String): Boolean {
        return password.windowed(3).filter { window ->
            window == "${window[0]}${window[0] + 1}${window[0] + 2}"
        }.any {
            it.isNotBlank()
        } && !password.contains(Regex("[oil]")) && password.windowed(2).filter {
            it[0] == it[1]
        }.distinct().size > 1
    }

    fun findNextPassword(password: String): String {
        val invalidLetters = setOf('o','i','l')
        var newPassword = password
        do {
            var increment = true

            newPassword = newPassword.reversed().map { c ->
                if (increment) {
                    if (c == 'z') {
                        'a'
                    } else {
                        increment = false
                        var newValue = c + 1
                        if (newValue in invalidLetters) {
                            newValue + 1
                        } else {
                            newValue
                        }
                    }
                } else {
                    c
                }
            }.joinToString(separator = "")
                    .reversed()

        } while(!validatePassword(newPassword))

        return newPassword
    }

    override fun partTwo(): Any {
        return findNextPassword(findNextPassword(inputString))
    }
}