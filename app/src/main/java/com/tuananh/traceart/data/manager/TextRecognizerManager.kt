package com.tuananh.traceart.data.manager

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class TextRecognizerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recognizer: TextRecognizer
) {
//    private var recognizer: TextRecognizer? = null
//
//    init {
//        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//    }

    suspend fun scanImageFromPath(imagePath: String): String? = suspendCoroutine { cont ->
        val inputImage = InputImage.fromFilePath(context, Uri.fromFile(File(imagePath)))
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val resultBuilder = StringBuilder()
                for (block in visionText.textBlocks) {
                    for (line in block.lines) {
                        resultBuilder.append(line.text).append("\n") // Xuống dòng theo line thật
                    }
                    resultBuilder.append("\n") // Tách block
                }
                val resultText = resultBuilder.toString()
                cont.resume(resultText)
            }
            .addOnFailureListener { e ->
                cont.resume(null)
            }
    }
}