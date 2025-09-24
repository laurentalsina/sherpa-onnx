package com.k2fsa.sherpa.onnx.tts.engine

import pl.allegro.finance.tradukisto.internal.Container

object TextNormalizer {
    private val frInt = Container.frenchContainer().getIntegerConverter()
    //private val enInt = Container.englishContainer().getIntegerConverter()

    // lang: "fra" or "eng" or "deu" etc
    @Synchronized
    fun normalize(text: String, lang: String?): String {
         String
        if (lang.equals("fra", true)) {
            val conv = frInt
        } else {
            // only french needs this
            return text
        }

        val regex = Regex("\\d+")
        return regex.replace(text) { m ->
            val n = m.value.toIntOrNull()
            if (n != null) conv.asWords(n) else m.value
        }
    }
}