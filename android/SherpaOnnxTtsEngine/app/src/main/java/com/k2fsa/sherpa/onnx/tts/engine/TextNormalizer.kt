package com.k2fsa.sherpa.onnx.tts.engine

import pl.allegro.finance.tradukisto.MoneyConverters

object TextNormalizer {
    private val converter = MoneyConverters.FRENCH_BANKING_MONEY_VALUE

    fun normalize(text: String): String {
        val regex = Regex("\\d+")
        return regex.replace(text) { matchResult ->
            val number = matchResult.value.toBigDecimalOrNull()
            if (number != null) {
                converter.asWords(number)
            } else {
                matchResult.value
            }
        }
    }
}