package lee.group.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    items: List<PieSlice>,
    title: String = String(),
    amountText: String = String(),
    chartSize: Dp = 200.dp,
    strokeWidth: Dp = 40.dp,
    startAngle: Float = -90f,
    padding: Dp = 24.dp,
) {
    val strokeWidthPx = strokeWidth.toPx()
    val total = items.sumOf { it.amount.toDouble() }.toLong()
    val backgroundColor = Color.White
    val emptyColor = Color.LightGray
    Box(
        modifier = modifier
            .padding(padding)
            .height(chartSize)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val radius = chartSize.toPx() / 2f
        Canvas(modifier = Modifier.size(chartSize)) {
            var angleStart = startAngle
            val size = Size((chartSize - strokeWidth).toPx(), (chartSize - strokeWidth).toPx())
            val stroke = Stroke(width = strokeWidthPx)
            val topLeft = Offset(strokeWidthPx / 2f, strokeWidthPx / 2f)

            items.forEach { item ->
                val sweepAngle = item.amount / total * 360f
                drawArc(
                    color = item.color,
                    startAngle = angleStart,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = size,
                    style = stroke,
                    topLeft = topLeft,
                )
                angleStart += sweepAngle
            }

            items.forEach { item ->
                val sweepAngle = item.amount / total * 360f
                drawLine(
                    color = backgroundColor,
                    start = center,
                    end = center.copy(
                        x = center.x + radius * cos(Math.toRadians(angleStart.toDouble())).toFloat(),
                        y = center.y + radius * sin(Math.toRadians(angleStart.toDouble())).toFloat()
                    ),
                    strokeWidth = 1.dp.toPx()
                )
                angleStart += sweepAngle
            }

            if (items.isEmpty()) {
                drawArc(
                    color = emptyColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size,
                    style = stroke,
                    topLeft = topLeft,
                )
            }
        }

        var angleStart = startAngle
        items.forEach { item ->
            val sweepAngle = item.amount / total * 360f
            val middleAngle = angleStart + sweepAngle / 2

            val label = item.label
            if (label.isNotBlank()) {
                var width by remember { mutableIntStateOf(0) }

                val x =
                    (radius + 15.dp.toPx()) * cos(Math.toRadians(middleAngle.toDouble())).toFloat()
                val y =
                    (radius + 15.dp.toPx()) * sin(Math.toRadians(middleAngle.toDouble())).toFloat()
                val adjustedX = if (middleAngle in 90f..270f) {
                    x - width / 2
                } else {
                    x + width / 2
                }

                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            width = it.size.width
                        }
                        .then(
                            Modifier.offset(x = adjustedX.toDp(), y = y.toDp())
                        ),
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            angleStart += sweepAngle
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            if (amountText.isNotBlank()) {
                Text(
                    text = amountText,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun DonutChartPreview() {
    val items = listOf(
        PieSlice(amount = 40f, color = Color(0xFF6C9D9B), label = "Marketing 40%"),
        PieSlice(amount = 30f, color = Color(0xFF8BAF9E), label = "R&D 30%"),
        PieSlice(amount = 20f, color = Color(0xFFB2A3C9), label = "Sales 20%"),
        PieSlice(amount = 10f, color = Color(0xFF9FB3C8), label = "Support 10%")
    )

    DonutChart(
        items = items,
        title = "Budget",
        amountText = "100 Billions",
        strokeWidth = 40.dp,
    )
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current.density
    return this.value * density
}

@Composable
fun Float.toDp(): Dp {
    val density = LocalDensity.current.density
    return Dp(this / density)
}