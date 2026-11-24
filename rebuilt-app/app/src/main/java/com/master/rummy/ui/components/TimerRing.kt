package com.master.rummy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.SuitRed

@Composable
fun TimerRing(
    remainingSeconds: Int,
    totalSeconds: Int = 30,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp
) {
    val progress = (remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

    val ringColor = when {
        remainingSeconds <= 5 -> SuitRed
        remainingSeconds <= 12 -> GoldAccent
        else -> Color(0xFF4CAF50)
    }

    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            // Background track
            drawCircle(
                color = Color.Black.copy(alpha = 0.4f),
                radius = (size.toPx() - strokeWidth.toPx()) / 2,
                style = Stroke(width = strokeWidth.toPx())
            )
            // Progress arc
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$remainingSeconds",
            color = ringColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
