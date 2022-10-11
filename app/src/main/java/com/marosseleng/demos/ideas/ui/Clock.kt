package com.marosseleng.demos.ideas.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.LocalTime

val clockFlow = flow<LocalTime> {
    // there are 1440 minutes in a day
    (0 until 1440).forEach { n ->
        // emit n-th minute
        val hour = n / 60
        emit(LocalTime.of(hour, n - (hour * 60)))
        delay(1000L)
    }
}

private fun Density.createStripeBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float
): Brush {
    val stripeWidthPx = stripeWidth.toPx()
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, 0f),
        tileMode = TileMode.Repeated
    )
}

@ExperimentalUnitApi
@Composable
fun Clock(modifier: Modifier, time: LocalTime) {
    val hourString = "%02d".format(time.hour)
    val minuteString = "%02d".format(time.minute)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp * 2)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(border = BorderStroke(Dp.Hairline, Color.Black), shape = CircleShape)
        )
        ClockDisc(
            modifier = Modifier,
            radius = 125.dp,
            shownNumber = minuteString[1].digitToInt(),
            numbers = (0..9).toList()
        )
        ClockDisc(
            modifier = Modifier,
            radius = 105.dp,
            shownNumber = minuteString[0].digitToInt(),
            numbers = (0..9).toList()
        )
        Box(
            modifier = Modifier
                .size(85.dp * 2)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(border = BorderStroke(Dp.Hairline, Color.Black), shape = CircleShape)
        )
        ClockDisc(
            modifier = Modifier,
            radius = 80.dp,
            shownNumber = hourString[1].digitToInt(),
            numbers = (0..9).toList()
        )
        ClockDisc(
            modifier = Modifier,
            radius = 60.dp,
            shownNumber = hourString[0].digitToInt(),
            numbers = listOf(0, 1, 2)
        )

        val widthPx = with(LocalDensity.current) { 5.dp.toPx() }
        val strokeWidthPx = with(LocalDensity.current) { 2.dp.toPx() }
        val brush = with(LocalDensity.current) { createStripeBrush(Color.Red, 2.dp, 0.6f) }
        Canvas(
            modifier = Modifier
                .size(130.dp * 2)
                .clip(CircleShape),
            onDraw = {
                val roundedRect = RoundRect(
                    Rect(
                        topLeft = center - Offset(0f, 40f),
                        bottomRight = center + Offset((size.width / 2) - widthPx, 40f)
                    ), CornerRadius(40f)
                )
                val rectanglePath = Path().apply {
                    addRoundRect(roundedRect)
                }
                drawRoundRect(
                    color = Color.Red,
                    topLeft = center - Offset(0f, 40f),
                    size = Size(
                        width = (size.width / 2) - widthPx,
                        height = 80f
                    ),
                    cornerRadius = CornerRadius(40f),
                    style = Stroke(width = strokeWidthPx)
                )
                clipPath(rectanglePath, clipOp = ClipOp.Difference) {
                    drawCircle(brush)
                }
                drawCircle(Color.Red, radius = (size.width / 2), style = Stroke(width = strokeWidthPx * 2))
            })
    }
}

@ExperimentalUnitApi
@Composable
fun ClockDisc(
    modifier: Modifier,
    radius: Dp,
    shownNumber: Int,
    numbers: List<Int>,
) {

    val angleStep = 23f
    val something: Float by animateFloatAsState(
        targetValue = 90f - (numbers.indexOf(shownNumber).toFloat() * angleStep),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    Box(
        modifier = modifier
            .size(radius * 2)
            .rotate(something)
            .clip(CircleShape)
            .background(Color.LightGray)
            .border(border = BorderStroke(Dp.Hairline, Color.Black), shape = CircleShape)
    ) {
        for ((index, number) in numbers.withIndex()) {
            val angleToRotate = angleStep * index
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
                    .rotate(angleToRotate)
                    .offset(0.dp, (radius - 10.dp) * -1)
                    .rotate(-90f)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "$number", fontWeight = FontWeight.Black, fontSize = TextUnit(16f, TextUnitType.Sp))
            }
        }
    }
}