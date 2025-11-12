package com.example.prm392_taskquest.ui.screens.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

data class DrawingPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(
    onNavigateBack: () -> Unit,
    onSave: (List<DrawingPath>) -> Unit
) {
    var paths by remember { mutableStateOf(listOf<DrawingPath>()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var currentStrokeWidth by remember { mutableStateOf(10f) }
    var showColorPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Draw Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onSave(paths)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        bottomBar = {
            DrawingToolbar(
                currentColor = currentColor,
                currentStrokeWidth = currentStrokeWidth,
                onColorSelected = { currentColor = it },
                onStrokeWidthChanged = { currentStrokeWidth = it },
                onClearAll = { paths = emptyList() },
                onUndo = {
                    if (paths.isNotEmpty()) {
                        paths = paths.dropLast(1)
                    }
                }
            )
        }
    ) { paddingValues ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath = Path().apply {
                                moveTo(offset.x, offset.y)
                            }
                        },
                        onDrag = { change, _ ->
                            currentPath?.lineTo(change.position.x, change.position.y)
                        },
                        onDragEnd = {
                            currentPath?.let { path ->
                                paths = paths + DrawingPath(
                                    path = path,
                                    color = currentColor,
                                    strokeWidth = currentStrokeWidth
                                )
                            }
                            currentPath = null
                        }
                    )
                }
        ) {
            // Draw all completed paths
            paths.forEach { drawingPath ->
                drawPath(
                    path = drawingPath.path,
                    color = drawingPath.color,
                    style = Stroke(
                        width = drawingPath.strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Draw current path being drawn
            currentPath?.let { path ->
                drawPath(
                    path = path,
                    color = currentColor,
                    style = Stroke(
                        width = currentStrokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

@Composable
fun DrawingToolbar(
    currentColor: Color,
    currentStrokeWidth: Float,
    onColorSelected: (Color) -> Unit,
    onStrokeWidthChanged: (Float) -> Unit,
    onClearAll: () -> Unit,
    onUndo: () -> Unit
) {
    val colors = listOf(
        Color.Black,
        Color(0xFFEF4444), // Red
        Color(0xFF3B82F6), // Blue
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Amber
        Color(0xFF8B5CF6), // Purple
        Color(0xFFEC4899), // Pink
        Color(0xFF06B6D4)  // Cyan
    )

    var showStrokeSlider by remember { mutableStateOf(false) }

    Column {
        if (showStrokeSlider) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Brush Size: ${currentStrokeWidth.toInt()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Slider(
                        value = currentStrokeWidth,
                        onValueChange = onStrokeWidthChanged,
                        valueRange = 5f..50f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color picker
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (currentColor == color) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Tools
                IconButton(onClick = { showStrokeSlider = !showStrokeSlider }) {
                    Icon(Icons.Default.Create, contentDescription = "Brush Size")
                }

                IconButton(onClick = onUndo) {
                    Icon(Icons.Default.Clear, contentDescription = "Undo")
                }

                IconButton(onClick = onClearAll) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear All")
                }
            }
        }
    }
}
