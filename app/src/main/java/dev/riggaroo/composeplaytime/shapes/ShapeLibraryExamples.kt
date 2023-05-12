package dev.riggaroo.composeplaytime.shapes

import android.graphics.Matrix
import android.graphics.RectF
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StampedPathEffectStyle.Companion.Morph
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.Star
import kotlin.math.min

/*
* Copyright 2022 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

class MorphPolygonShape(
    private val shapeA: RoundedPolygon,
    private val shapeB: RoundedPolygon,
    private val percentage: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrixA = calculateMatrix(shapeA.bounds, size.width, size.height)
        shapeA.transform(matrixA)
        val matrixB = calculateMatrix(shapeB.bounds, size.width, size.height)
        shapeB.transform(matrixB)
        val morph = Morph(shapeA, shapeB)
        morph.progress = percentage
        return Outline.Generic(morph.asPath().asComposePath())
    }
}
@Preview
@Composable
fun ShapeAsClip() {
    val shapeA = RoundedPolygon(5, rounding = CornerRounding(0.2f))
    val shapeB = Star(5, rounding = CornerRounding(0.8f))
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(targetValue = if (isPressed) 1f else 0f,
        label = "progress", animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    Box(modifier = Modifier
        .padding(8.dp)
        .clip(MorphPolygonShape(shapeA, shapeB, animatedProgress.value))
        .background(Color(0xFF80DEEA))
        .size(200.dp)
        .clickable(interactionSource = interactionSource, indication = null) {
        }
    ) {
    }
}

internal fun calculateMatrix(bounds: RectF, width: Float, height: Float): Matrix {
    val originalWidth = bounds.right - bounds.left
    val originalHeight = bounds.bottom - bounds.top
    val scale = min(width / originalWidth, height / originalHeight)
    val newLeft = bounds.left - (width / scale - originalWidth) / 2
    val newTop = bounds.top - (height / scale - originalHeight) / 2
    val matrix = Matrix()
    matrix.setTranslate(-newLeft, -newTop)
    matrix.postScale(scale, scale)
    return matrix
}