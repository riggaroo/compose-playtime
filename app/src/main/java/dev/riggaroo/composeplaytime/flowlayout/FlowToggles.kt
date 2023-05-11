package dev.riggaroo.composeplaytime.flowlayout

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowToggles() {
    var selectedOption by remember {
        mutableStateOf("1gb")
    }
    val items = listOf("1", "5", "10", "20")
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF4678F3), Color(0xFF64D6EE))))
    ) {
        items.forEach { amount ->
            DataBundleSizeToggleOption(selectedOption == amount, amount, onSelected =  {
                selectedOption = amount
            })
        }
    }
}

@Composable
private fun RowScope.DataBundleSizeToggleOption(
    isSelected: Boolean,
    displayAmount: String,
    onSelected: (String) -> Unit
    ) {
    val updateTransition =
        updateTransition(targetState = isSelected, label = "transition")
    val animatedWeight = updateTransition.animateFloat(
        label = "weight",
        transitionSpec = {
            tween(
                800,
                easing = CubicBezierEasing(.98f, 0f, .22f, .98f)
            )
        }
    ) { selected ->
        if (selected) {
            1.5f
        } else {
            1f
        }
    }
    val animatedLine = updateTransition.animateFloat(
        label = "line",
        transitionSpec = {
            tween(
                800,
                easing = CubicBezierEasing(.98f, 0f, .22f, .98f)
            )
        }
    ) { selected ->
        if (selected) {
            1f
        } else {
            0.2f
        }
    }
    val animatedOpacity = updateTransition.animateFloat(label = "opacity",
        transitionSpec = {
            tween(
                600,
                easing = EaseInOut
            )
        }
    ) { selected ->
        if (selected) {
            1f
        } else {
            0.5f
        }
    }
    val animatedTextSize = updateTransition.animateFloat(label = "textSize",
        transitionSpec = {
            tween(
                600,
                easing = EaseInOut
            )
        }
    ) { selected ->
        if (selected) {
            50f
        } else {
            28f
        }
    }
    Box(modifier = Modifier.Companion
        .weight(animatedWeight.value)
        .alpha(animatedOpacity.value)
        .fillMaxWidth()
        .height(150.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
        .pointerInput(displayAmount) {
            detectTapGestures {
                onSelected(displayAmount)
            }
        }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 16.dp)
        ) {
            Text(
                displayAmount,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .alignByBaseline(),
                fontSize = animatedTextSize.value.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Text(
                "GB",
                modifier = Modifier.alignByBaseline(),
                fontSize = animatedTextSize.value.sp * 0.6f,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                .background(Color(0xFF64D6EE))
                .height(6.dp)
                .fillMaxWidth(animatedLine.value)
                .align(Alignment.BottomStart)
        )
        Box(modifier = Modifier
            .padding(8.dp)
            .drawBehind {
                if (isSelected) {
                    drawCircle(Color(0xFF64D6EE))
                } else {
                    drawCircle(Color(0xFF64D6EE), style = Stroke(2.dp.toPx()))
                }
            }
            .size(16.dp)
            .align(Alignment.TopEnd)
        )
    }
}

