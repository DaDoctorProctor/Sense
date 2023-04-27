package com.example.homerosense.presentation.screen.monitor

import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerosense.presentation.TempHumidityViewModel
import kotlinx.coroutines.delay

const val yStep = 5
val pointsStart = mutableListOf(0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f)

@Composable
fun Chart(viewModel: TempHumidityViewModel){
    Box(contentAlignment = Alignment.Center
    ){
        // create Box with canvas
        Box(modifier = Modifier
            .padding(10.dp)
            .height(200.dp)
            .fillMaxWidth()
            .background(color = Color.White, RoundedCornerShape(15.dp)))
        {
            var isButtonVisible by remember { mutableStateOf(true) }
            if (isButtonVisible) {
                pointsStart.add(viewModel.temperature)
                val points = pointsStart.takeLast(10)
                val sizeP = pointsStart.size
//            Log.d("TemperatureUpdate","Updated $sizeP")
//            Log.d("Counter", "$counter")
                Graph(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    xValues = (0..9).map { it + 1 },
                    yValues = (0..6).map { (it + 1) * yStep },
                    points = points,
                    paddingSpace = 16.dp,
                    verticalStep = yStep
                )
            }
            LaunchedEffect(Unit) {
                while(true){
                    isButtonVisible = true
                    delay(999)
                    isButtonVisible = false
                    delay(1)
                }
            }



        }
    }
}


@Composable
fun Graph(
    modifier : Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: List<Float>,
    paddingSpace: Dp,
    verticalStep: Int
) {
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / yValues.size
            /** placing x axis points */
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${xValues[i]}",
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }
            /** placing y axis points */
            for (i in yValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${yValues[i]}",
                    paddingSpace.toPx() / 2f,
                    size.height - yAxisSpace * (i + 1),
                    textPaint
                )
            }
            /** placing our x axis points */
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height - (yAxisSpace * (points[i]/verticalStep.toFloat()))
                coordinates.add(PointF(x1,y1))
                /** drawing circles to indicate all the points */
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = Offset(x1,y1)
                )
            }
//            /** calculating the connection points */
//            for (i in 1 until coordinates.size) {
//                controlPoints1.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i - 1].y))
//                controlPoints2.add(PointF((coordinates[i].x + coordinates[i - 1].x) / 2, coordinates[i].y))
//            }
//            /** drawing the path */
//            val stroke = Path().apply {
//                reset()
//                moveTo(coordinates.first().x, coordinates.first().y)
//                for (i in 0 until coordinates.size - 1) {
//                    cubicTo(
//                        controlPoints1[i].x,controlPoints1[i].y,
//                        controlPoints2[i].x,controlPoints2[i].y,
//                        coordinates[i + 1].x,coordinates[i + 1].y
//                    )
//                }
//            }
//            /** filling the area under the path */
//            val fillPath = android.graphics.Path(stroke.asAndroidPath())
//                .asComposePath()
//                .apply {
//                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
//                    lineTo(xAxisSpace, size.height - yAxisSpace)
//                    close()
//                }
//            drawPath(
//                fillPath,
//                brush = Brush.verticalGradient(
//                    listOf(
//                        Color.Cyan,
//                        Color.Transparent,
//                    ),
//                    endY = size.height - yAxisSpace
//                ),
//            )
//            drawPath(
//                stroke,
//                color = Color.Black,
//                style = Stroke(
//                    width = 5f,
//                    cap = StrokeCap.Round
//                )
//            )
        }
    }
}