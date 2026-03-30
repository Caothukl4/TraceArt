package com.tuananh.traceart.presentation.screen.drawing

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.tuananh.traceart.presentation.navigation.LocalNavController
import com.tuananh.traceart.presentation.navigation.Routes
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(
    imageUri: String,
    viewModel: DrawingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isZoomMode by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    
    val strokeWidth by viewModel.strokeWidth
    val strokeColor by viewModel.strokeColor
    val strokeAlpha by viewModel.strokeAlpha
    val isEraserMode by viewModel.isEraserMode

    LaunchedEffect(Unit) {
        viewModel.saveStatus.collectLatest { success ->
            if (success) {
                Toast.makeText(context, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Save Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Trace Art", 
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF1B1B1B))
                    }
                },
                actions = {
                    IconButton(onClick = { isZoomMode = !isZoomMode }) {
                        Icon(
                            imageVector = if (isZoomMode) Icons.Default.Create else Icons.Default.Search, 
                            contentDescription = if (isZoomMode) "Switch to Draw" else "Switch to Zoom", 
                            tint = Color(0xFF1B1B1B)
                        )
                    }
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(Icons.Default.Undo, contentDescription = null, tint = Color(0xFF1B1B1B))
                    }
                    IconButton(onClick = { 
                        scope.launch {
                            isCapturing = true
                            kotlinx.coroutines.delay(100)
                            
                            try {
                                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                viewModel.saveDrawingToGallery(context, bitmap)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                isCapturing = false
                            }
                            
                            val score = viewModel.calculateScore()
                            navController.navigate(Routes.result(score))
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ECCA3))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1B1B1B)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFBFBFB))
                .pointerInput(isZoomMode) {
                    if (isZoomMode) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    }
                }
        ) {
            // Content to capture
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .drawWithContent {
                        graphicsLayer.record {
                            if (isCapturing) {
                                drawRect(Color.White)
                            }
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    }
            ) {
                // Reference Image
                if (!isCapturing) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        alpha = 0.5f
                    )
                }

                // Canvas for Drawing
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .pointerInput(isZoomMode) {
                        if (!isZoomMode) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    viewModel.addPoint(offset)
                                },
                                onDrag = { change, _ ->
                                    viewModel.addPoint(change.position)
                                },
                                onDragEnd = {
                                    viewModel.finishPath()
                                }
                            )
                        }
                    }
            ) {
                // Draw existing paths
                viewModel.paths.forEach { path ->
                    val drawPath = Path().apply {
                        if (path.points.isNotEmpty()) {
                            moveTo(path.points.first().x, path.points.first().y)
                            path.points.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                    }
                    drawPath(
                        path = drawPath,
                        color = if (path.isEraser) Color.Transparent else path.color,
                        style = Stroke(width = path.strokeWidth, cap = StrokeCap.Round),
                        blendMode = if (path.isEraser) BlendMode.Clear else BlendMode.SrcOver
                    )
                }

                // Draw current path
                val currentPath = Path().apply {
                    if (viewModel.currentPoints.isNotEmpty()) {
                        moveTo(viewModel.currentPoints.first().x, viewModel.currentPoints.first().y)
                        viewModel.currentPoints.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                }
                drawPath(
                    path = currentPath,
                    color = if (isEraserMode) Color.Transparent else strokeColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    blendMode = if (isEraserMode) BlendMode.Clear else BlendMode.SrcOver
                )
            }
        }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val colors = listOf(
                    Color(0xFF1B1B1B), // Black
                    Color(0xFFFF3B30), // Red
                    Color(0xFFFF9500), // Orange
                    Color(0xFFFFCC00), // Yellow
                    Color(0xFF4CD964), // Green
                    Color(0xFF5AC8FA), // Light Blue
                    Color(0xFF007AFF), // Blue
                    Color(0xFF5856D6), // Purple
                    Color(0xFFFF2D55)  // Pink
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        val isSelected = isEraserMode
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) Color(0xFF1B1B1B).copy(alpha = 0.1f) else Color.Transparent)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF1B1B1B) else Color.LightGray,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable { viewModel.toggleEraserMode() }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Erase", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1B1B))
                        }
                    }

                    items(colors) { color ->
                        val isSelected = !isEraserMode && strokeColor.copy(alpha = 1f) == color
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF1B1B1B).copy(alpha = 0.5f) else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable { viewModel.updateStrokeColor(color) }
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Slider(
                        value = strokeAlpha,
                        onValueChange = { viewModel.updateStrokeAlpha(it) },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        valueRange = 0.1f..1f,
                        enabled = !isEraserMode,
                        colors = SliderDefaults.colors(
                            thumbColor = strokeColor.copy(alpha = 1f),
                            activeTrackColor = strokeColor.copy(alpha = 1f).copy(alpha = 0.5f),
                            inactiveTrackColor = Color.LightGray
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(Color.White, CircleShape)
                                    .border(2.dp, strokeColor, CircleShape)
                            )
                        }
                    )
                }

                // Thickness Menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(4f, 8f, 16f, 24f).forEach { width ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (strokeWidth == width) Color(0xFF1B1B1B).copy(alpha = 0.1f) else Color.Transparent)
                                .border(
                                    width = if (strokeWidth == width) 2.dp else 0.dp,
                                    color = if (strokeWidth == width) Color(0xFF1B1B1B) else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { viewModel.updateStrokeWidth(width) },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size((width / 2).coerceIn(4f, 20f).dp)
                                    .background(if (strokeWidth == width) Color(0xFF1B1B1B) else Color.Gray, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}



