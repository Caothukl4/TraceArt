package com.tuananh.traceart.presentation.screen.drawing

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.tuananh.traceart.domain.model.DrawingPath
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.tuananh.traceart.presentation.util.DrawUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DrawingViewModel @Inject constructor() : ViewModel() {
    private val _paths = mutableStateListOf<DrawingPath>()
    val paths: List<DrawingPath> = _paths

    private val _currentPoints = mutableStateListOf<Offset>()
    val currentPoints: List<Offset> = _currentPoints

    private val _strokeWidth = mutableStateOf(6f)
    val strokeWidth: State<Float> = _strokeWidth

    private val _strokeColor = mutableStateOf(Color(0xFF1B1B1B))
    val strokeColor: State<Color> = _strokeColor

    private val _strokeAlpha = mutableStateOf(1f)
    val strokeAlpha: State<Float> = _strokeAlpha

    private val _isEraserMode = mutableStateOf(false)
    val isEraserMode: State<Boolean> = _isEraserMode

    private val _saveStatus = MutableSharedFlow<Boolean>()
    val saveStatus = _saveStatus.asSharedFlow()

    fun addPoint(offset: Offset) {
        _currentPoints.add(offset)
    }

    fun finishPath() {
        if (_currentPoints.isNotEmpty()) {
            _paths.add(
                DrawingPath(
                    points = _currentPoints.toList(),
                    color = _strokeColor.value,
                    strokeWidth = _strokeWidth.value,
                    isEraser = _isEraserMode.value
                )
            )
            _currentPoints.clear()
        }
    }

    fun updateStrokeWidth(width: Float) {
        _strokeWidth.value = width
    }

    fun toggleEraserMode() {
        _isEraserMode.value = !_isEraserMode.value
    }

    fun updateStrokeColor(color: Color) {
        _isEraserMode.value = false
        _strokeColor.value = color.copy(alpha = _strokeAlpha.value)
    }

    fun updateStrokeAlpha(alpha: Float) {
        _strokeAlpha.value = alpha
        _strokeColor.value = _strokeColor.value.copy(alpha = alpha)
    }

    fun undo() {
        if (_paths.isNotEmpty()) {
            _paths.removeAt(_paths.size - 1)
        }
    }

    fun saveDrawingToGallery(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            val fileName = "TraceArt_${System.currentTimeMillis()}"
            val uri = DrawUtils.saveBitmapToGallery(context, bitmap, fileName)
            _saveStatus.emit(uri != null)
        }
    }

    fun calculateScore(): Int {

        // Simple logic for demonstration
        val totalPoints = _paths.sumOf { it.points.size }
        return when {
            totalPoints > 200 -> Random.nextInt(90, 100)
            totalPoints > 100 -> Random.nextInt(75, 90)
            totalPoints > 50 -> Random.nextInt(50, 75)
            totalPoints > 0 -> Random.nextInt(10, 50)
            else -> 0
        }
    }
}

