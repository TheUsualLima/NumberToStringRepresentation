package com.example.numbertostringrepresentation.domain.numtotextparser

class EngNumToTextRepParser : NumToTextRepParser {

    override fun parse(num: Long): String {
        if (num == 0L) return "Zero"

        var result = ""
        val workingList = mutableListOf<Int>()

        var remainingNum = num
        while (remainingNum > 0) {
            workingList.add((remainingNum % 1000).toInt())
            remainingNum /= 1000
        }

        var quantifierPosition = -1
        for (subNum in workingList) {
            val tempResult = mutableListOf<String>()
            val hundreds = subNum / 100
            val tens = subNum % 100
            val ones = subNum % 10

            if (hundreds > 0) {
                determineHundreds(hundreds)?.let(tempResult::add)
            }
            if ((tens >= 10 || ones > 0) && tempResult.isNotEmpty()) {
                tempResult.add("and")
            }
            if (tens >= 10) {
                determineTens(tens)?.let(tempResult::add)
            }
            if (ones > 0 && tens !in 10 until 19) {
                determineSingleDigit(ones)?.let(tempResult::add)
            }

            if (tempResult.isNotEmpty()) {
                if (quantifierPosition >= 0) {
                    tempResult.add("${quantifierList[quantifierPosition]},")
                }
                result = "${tempResult.joinToString(separator = " ")} $result"
            }
            quantifierPosition++
        }
        return result
    }

    private fun determineSingleDigit(num: Int): String? =
        SingleDigits.values().getOrNull(num - 1)?.textValue

    private fun determineTens(num: Int): String? =
        teens[num] ?: DoubleDigits.values().getOrNull((num / 10) - 2)?.textValue

    private fun determineHundreds(num: Int): String? =
        determineSingleDigit(num)?.let { "$it Hundred" }

    private enum class SingleDigits(val textValue: String) {
        ONE("One"), TWO("Two"), THREE("Three"),
        FOUR("Four"), FIVE("Five"), SIX("Six"), SEVEN("Seven"),
        EIGHT("Eight"), NINE("Nine")
    }

    private enum class DoubleDigits(val textValue: String) {
        TWENTY("Twenty"), THIRTY("Thirty"), FORTY("Forty"),
        FIFTY("Fifty"), SIXTY("Sixty"), SEVENTY("Seventy"),
        EIGHTY("Eighty"), NINETY("Ninety"),
    }

    private companion object {
        val quantifierList = listOf(
            "Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintillion"
        )
        val teens = mapOf(
            10 to "Ten",
            11 to "Eleven",
            12 to "Twelve",
            13 to "Thirteen",
            14 to "Fourteen",
            15 to "Fifteen",
            16 to "Sixteen",
            17 to "Seventeen",
            18 to "Eighteen",
            19 to "Nineteen",
        )
    }
}
