package com.k2fsa.sherpa.onnx.tts.engine

import PreferenceHelper
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.getOfflineTtsConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object TtsEngine {
    var tts: OfflineTts? = null
    private var currentModel: TtsModel? = null

    // https://en.wikipedia.org/wiki/ISO_639-3
    // Example:
    // eng for English,
    // deu for German
    // cmn for Mandarin
    var lang: String? = null

    // if a model supports two languages, set also lang2
    var lang2: String? = null


    val speedState: MutableState<Float> = mutableFloatStateOf(1.0F)
    val speakerIdState: MutableState<Int> = mutableIntStateOf(0)

    var speed: Float
        get() = speedState.value
        set(value) {
            speedState.value = value
        }

    var speakerId: Int
        get() = speakerIdState.value
        set(value) {
            speakerIdState.value = value
        }

    private var assets: AssetManager? = null

    fun createTts(context: Context, model: TtsModel = allModels[0]) {
        Log.i(TAG, "Init Next-gen Kaldi TTS")
        if (tts == null || model != currentModel) {
            currentModel = model
            initTts(context, model)
        }
    }

    fun recreateTts(context: Context, model: TtsModel) {
        tts = null
        createTts(context, model)
    }

    private fun initTts(context: Context, model: TtsModel) {
        assets = context.assets

        var modelDir = model.modelDir
        var modelName = model.modelName
        var acousticModelName = model.acousticModelName
        var vocoder = model.vocoder
        var voices = model.voices
        var ruleFsts = model.ruleFsts
        var ruleFars = model.ruleFars
        var lexicon = model.lexicon
        var dataDir = model.dataDir
        var dictDir = model.dictDir
        var isKitten = model.isKitten

        lang = model.lang
        lang2 = model.lang2

        if (dataDir != null) {
            val newDir = copyDataDir(context, dataDir)
            dataDir = "$newDir/$dataDir"
        }

        if (dictDir != null) {
            val newDir = copyDataDir(context, dictDir)
            dictDir = "$newDir/$dictDir"
            if (ruleFsts == null) {
                ruleFsts = "$modelDir/phone.fst,$modelDir/date.fst,$modelDir/number.fst"
            }
        }

        val config = getOfflineTtsConfig(
            modelDir = modelDir,
            modelName = modelName,
            acousticModelName = acousticModelName ?: "",
            vocoder = vocoder ?: "",
            voices = voices ?: "",
            lexicon = lexicon ?: "",
            dataDir = dataDir ?: "",
            dictDir = dictDir ?: "",
            ruleFsts = ruleFsts ?: "",
            ruleFars = ruleFars ?: "",
            isKitten = isKitten,
        )

        speed = PreferenceHelper(context).getSpeed()
        speakerId = PreferenceHelper(context).getSid()

        tts = OfflineTts(assetManager = assets, config = config)
    }


    private fun copyDataDir(context: Context, dataDir: String): String {
        Log.i(TAG, "data dir is $dataDir")
        copyAssets(context, dataDir)

        val newDataDir = context.getExternalFilesDir(null)!!.absolutePath
        Log.i(TAG, "newDataDir: $newDataDir")
        return newDataDir
    }

    private fun copyAssets(context: Context, path: String) {
        val assets: Array<String>?
        try {
            assets = context.assets.list(path)
            if (assets!!.isEmpty()) {
                copyFile(context, path)
            } else {
                val fullPath = "${context.getExternalFilesDir(null)}/$path"
                val dir = File(fullPath)
                dir.mkdirs()
                for (asset in assets.iterator()) {
                    val p: String = if (path == "") "" else "$path/"
                    copyAssets(context, p + asset)
                }
            }
        } catch (ex: IOException) {
            Log.e(TAG, "Failed to copy $path. $ex")
        }
    }

    private fun copyFile(context: Context, filename: String) {
        try {
            val istream = context.assets.open(filename)
            val newFilename = context.getExternalFilesDir(null).toString() + "/" + filename
            val ostream = FileOutputStream(newFilename)
            // Log.i(TAG, "Copying $filename to $newFilename")
            val buffer = ByteArray(1024)
            var read = 0
            while (read != -1) {
                ostream.write(buffer, 0, read)
                read = istream.read(buffer)
            }
            istream.close()
            ostream.flush()
            ostream.close()
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to copy $filename, $ex")
        }
    }
}
