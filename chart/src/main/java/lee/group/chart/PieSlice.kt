package lee.group.chart

import androidx.compose.ui.graphics.Color

data class PieSlice(
    val amount: Float,
    val color: Color,
    val label: String
)