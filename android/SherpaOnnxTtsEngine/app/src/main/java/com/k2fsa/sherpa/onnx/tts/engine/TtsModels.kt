package com.k2fsa.sherpa.onnx.tts.engine

data class TtsModel(val modelDir: String, val modelName: String, val lang: String, val ruleFars: String? = null, val lexicon: String? = null, val dataDir: String? = null, val dictDir: String? = null, val lang2: String? = null, val acousticModelName: String? = null, val vocoder: String? = null, val voices: String? = null, val ruleFsts: String? = null, val isKitten: Boolean = false)

val allModels = listOf(
    TtsModel(
        modelDir = "vits-piper-en_US-amy-low",
        modelName = "en_US-amy-low.onnx",
        dataDir = "vits-piper-en_US-amy-low/espeak-ng-data",
        lang = "eng",
    ),
    TtsModel(
        modelDir = "vits-piper-de_DE-thorsten-low",
        modelName = "de_DE-thorsten-low.onnx",
        dataDir = "vits-piper-de_DE-thorsten-low/espeak-ng-data",
        lang = "deu",
    )
)
