package lee.group.composechart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lee.group.chart.DonutChart
import lee.group.chart.PieSlice
import lee.group.composechart.ui.theme.ComposeChartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeChartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
