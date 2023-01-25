package com.eliascoelho911.homeapp.ds.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun VerticalControl(
    modifier: Modifier = Modifier,
    valueLeft: Float,
    valueRight: Float,
    onValueLeftChanged: (Float) -> Unit,
    onValueRightChanged: (Float) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VerticalSlider(
            value = valueLeft,
            onValueChange = onValueLeftChanged
        )

        VerticalSlider(
            value = valueRight,
            onValueChange = onValueRightChanged
        )
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun VerticalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    /*@IntRange(from = 0)*/
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            other = Modifier
                .graphicsLayer {
                    rotationZ = 270f
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }
        ),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        interactionSource = interactionSource,
        thumb = remember(interactionSource, colors, enabled) {
            {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    colors = colors,
                    enabled = enabled,
                    thumbSize = DpSize(48.dp, 48.dp)
                )
            }
        },
        track = remember(Unit) {
            { sliderPositions ->
                Track(
                    sliderPositions = sliderPositions
                )
            }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun Track(
    sliderPositions: SliderPositions,
    modifier: Modifier = Modifier
) {
    val inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
    val activeTrackColor = MaterialTheme.colorScheme.primary
    val inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeTickColor = MaterialTheme.colorScheme.onPrimary
    val trackHeight = 36.dp
    val tickSize = 2.dp
    Canvas(
        modifier
            .fillMaxWidth()
            .height(trackHeight)
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        val tickSizePx = tickSize.toPx()
        val trackStrokeWidth = trackHeight.toPx()
        drawLine(
            inactiveTrackColor,
            sliderStart,
            sliderEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        val sliderValueEnd = Offset(
            sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * sliderPositions.positionFraction,
            center.y
        )

        val sliderValueStart = Offset(
            sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * 0f,
            center.y
        )

        drawLine(
            activeTrackColor,
            sliderValueStart,
            sliderValueEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        sliderPositions.tickFractions.groupBy {
            it > sliderPositions.positionFraction ||
                    it < 0f
        }.forEach { (outsideFraction, list) ->
            drawPoints(
                list.map {
                    Offset(lerp(sliderStart, sliderEnd, it).x, center.y)
                },
                PointMode.Points,
                if (outsideFraction) inactiveTickColor else activeTickColor,
                tickSizePx,
                StrokeCap.Round
            )
        }
    }
}