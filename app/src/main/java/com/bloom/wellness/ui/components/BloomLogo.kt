package com.bloom.wellness.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bloom.wellness.ui.theme.BloomGold
import com.bloom.wellness.ui.theme.BloomMaroon

/** The four-petal flower mark used on the splash screen, onboarding, and launcher icon. */
@Composable
fun BloomFlowerLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    Canvas(modifier = modifier.size(size)) {
        val petal = Path().apply {
            val w = this@Canvas.size.width
            val h = this@Canvas.size.height
            moveTo(w / 2f, h / 2f)
            cubicTo(w * 0.35f, h / 2f, w * 0.28f, h * 0.30f, w / 2f, h * 0.04f)
            cubicTo(w * 0.72f, h * 0.30f, w * 0.65f, h / 2f, w / 2f, h / 2f)
        }
        listOf(0f, 90f, 180f, 270f).forEach { angle ->
            rotate(degrees = angle, pivot = Offset(this.size.width / 2f, this.size.height / 2f)) {
                drawPath(petal, color = BloomMaroon)
            }
        }
        drawCircle(color = BloomGold, radius = this.size.minDimension * 0.09f, center = center)
    }
}

/** Small four-point sparkle accent scattered around the splash/onboarding backgrounds. */
@Composable
fun BloomSparkle(modifier: Modifier = Modifier, size: Dp = 10.dp, color: androidx.compose.ui.graphics.Color = BloomGold) {
    Canvas(modifier = modifier.size(size)) {
        val path = Path().apply {
            val w = this@Canvas.size.width
            val h = this@Canvas.size.height
            moveTo(w / 2f, 0f)
            cubicTo(w / 2f, h * 0.4f, w * 0.6f, h / 2f, w, h / 2f)
            cubicTo(w * 0.6f, h / 2f, w / 2f, h * 0.6f, w / 2f, h)
            cubicTo(w / 2f, h * 0.6f, w * 0.4f, h / 2f, 0f, h / 2f)
            cubicTo(w * 0.4f, h / 2f, w / 2f, h * 0.4f, w / 2f, 0f)
            close()
        }
        drawPath(path, color = color)
    }
}

/** Decorative sparkle accents positioned loosely like the Figma splash/onboarding frames. */
@Composable
fun BloomSparkleField(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        BloomSparkle(modifier = Modifier.offset(x = 28.dp, y = 40.dp), size = 8.dp)
        BloomSparkle(modifier = Modifier.offset(x = (-18).dp, y = 120.dp), size = 6.dp)
        BloomSparkle(modifier = Modifier.offset(x = 220.dp, y = 30.dp), size = 7.dp)
        BloomSparkle(modifier = Modifier.offset(x = 250.dp, y = 140.dp), size = 5.dp)
    }
}
