package com.k2fsa.sherpa.onnx.tts.engine

import pl.allegro.finance.tradukisto.internal.Container

object TextNormalizer {
    private val frInt = Container.frenchContainer().getIntegerConverter()
    //private val enInt = Container.englishContainer().getIntegerConverter()

    // lang: "fra" or "eng" or "deu" etc
    @Synchronized
    fun normalize(text: String, lang: String?): String {
        val cleanedText = text.replace(Regex("[^\\w\\s]"), "")
        if (lang.equals("fra", true)) {
            val conv = frInt
            val regex = Regex("\\d+")
            return regex.replace(cleanedText) { m ->
                val n = m.value.toIntOrNull()
                if (n != null) conv.asWords(n) else m.value
            }
        } else {
            // only french needs this so far
            return cleanedText
        }
    }
}