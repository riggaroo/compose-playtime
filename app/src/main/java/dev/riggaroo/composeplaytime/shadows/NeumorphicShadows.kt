package dev.riggaroo.composeplaytime.shadows

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.DropShadow
import androidx.compose.ui.graphics.shadow.InnerShadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.riggaroo.composeplaytime.R

/*
* Copyright 2025 The Android Open Source Project
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
@Composable
@Preview(showBackground = true)
fun SimpleDropShadowUsage() {
    val pinkColor = Color(0xFFe91e63)
    val purpleColor = Color(0xFF9c27b0)
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .dropShadow(
                    RoundedCornerShape(20.dp),
                    dropShadow = DropShadow(
                        15.dp,
                        color = pinkColor,
                        spread = 10.dp,
                        alpha = 0.5f
                    )
                )
                .background(
                    purpleColor,
                    shape = RoundedCornerShape(20.dp)
                )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SimpleInnerShadowUsage() {
    val pinkColor = Color(0xFFe91e63)
    val purpleColor = Color(0xFF9c27b0)
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .background(
                    purpleColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .innerShadow(
                    RoundedCornerShape(20.dp),
                    innerShadow = InnerShadow(
                        15.dp,
                        color = Color.Black,
                        spread = 10.dp,
                        alpha = 0.5f
                    )
                )
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview(showBackground = true)
fun PhotoInnerShadowExample() {
    Box(Modifier.fillMaxSize()) {
        val shape = RoundedCornerShape(20.dp)
        Box(
            Modifier
                .size(200.dp)
                .align(Alignment.Center)
                // ensure your background is drawn before the inner shadow
                // as the inner shadow needs to sit on top of the content
                .background(
                    Color(0xfffec5bb),
                    shape = shape
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.cape_town),
                contentDescription = "Image with Inner Shadow",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .innerShadow(
                        shape,
                        innerShadow = InnerShadow(
                            15.dp,
                            spread = 15.dp
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
fun NeumorphicRecessedButton(
    shape: RoundedCornerShape = RoundedCornerShape(30.dp)
) {
    val bgColor = Color(0xFFe0e0e0)
    val lightShadow = Color(0xFFFFFFFF)
    val darkShadow = Color(0xFFb1b1b1)
    val upperOffset = -(10.dp)
    val lowerOffset = 10.dp
    val radius = 10.dp
    val spread = 0.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .wrapContentSize(Alignment.Center)
            .size(240.dp)
            .background(bgColor, shape)
            .innerShadow(
                shape, innerShadow = InnerShadow(
                    radius = radius,
                    color = darkShadow,
                    spread = spread,
                    offset = DpOffset(lowerOffset, lowerOffset)
                )
            )
            .innerShadow(
                shape, innerShadow = InnerShadow(
                    radius = radius,
                    color = lightShadow,
                    spread = spread,
                    offset = DpOffset(upperOffset, upperOffset)
                )
            )
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun NeumorphicRaisedButton(
    shape: RoundedCornerShape = RoundedCornerShape(30.dp)
) {
    val bgColor = Color(0xFFe0e0e0)
    val lightShadow = Color(0xFFFFFFFF)
    val darkShadow = Color(0xFFb1b1b1)
    val upperOffset = -10.dp
    val lowerOffset = 10.dp
    val radius = 15.dp
    val spread = 0.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .wrapContentSize(Alignment.Center)
            .size(240.dp)
            .dropShadow(
                shape,
                dropShadow = DropShadow(
                    radius = radius,
                    color = lightShadow,
                    spread = spread,
                ), offset = DpOffset(upperOffset, upperOffset)
            )
            .dropShadow(
                shape, dropShadow = DropShadow(
                    radius = radius,
                    color = darkShadow, spread = spread
                ),
                offset = DpOffset(lowerOffset, lowerOffset)
            )
            .background(bgColor, shape)
    )
}

@Composable
fun PressableNeumorphicButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable default ripple, Neumorphic change is the indication
                onClick = onClick
            )
    ) {
        if (isPressed) {
            NeumorphicRecessedButton()
        } else {
            NeumorphicRaisedButton()
        }
    }
}

@Preview
@Composable
private fun NeumorphicButtonPressPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe0e0e0))
            .wrapContentSize(Alignment.Center)
    ) {
        PressableNeumorphicButton(onClick = { /* Handle button click here */ })
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun GradientBasedDropShadow() {
    val shadowShape = MaterialShapes.Cookie9Sided.toShape() // or just a simple RoundedRect

    /**
     * While DropShadow and InnerShadow can be drawn with with arbitrary colors, the shadow
     * geometry itself can also be used as a mask in order to paint the shadow as a
     * gradient instead.
     * This can be accomplished by leveraging a GraphicsLayer with an offscreen
     * compositing layer, and drawing the gradient or Brush with the same geometry
     * with BlendMode.SrcIn
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .size(240.dp)
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithCache {
                // We can pass a sweepGradient into the dropShadow modifier, however it doesn't have access to the
                // canvas size easily.
                val sweep = ShaderBrush(
                    SweepGradientShader(
                        Offset(size.width / 2f, size.height / 2f),
                        listOf(
                            Color(0xFFf72585),
                            Color(0xFFb5179e),
                            Color(0xFF7209b7),
                            Color(0xFF560bad),
                            Color(0xFF480ca8),
                            Color(0xFF3a0ca3),
                            Color(0xFF3f37c9),
                            Color(0xFF4361ee),
                            Color(0xFF4895ef),
                            Color(0xFF4cc9f0),
                            Color(0xFFf72585),
                        )
                    )
                )
                onDrawWithContent {
                    drawContent()
                    // here we draw the shadow with SrcIn blend Mode which only draws the gradient where the
                    // shadow is.
                    drawRect(brush = sweep, blendMode = BlendMode.SrcIn)
                }
            }
            .innerShadow(
                shadowShape,
                innerShadow = InnerShadow(
                    radius = 15.dp,
                    color = Color.Black,
                    spread = 15.dp,
                    offset = DpOffset(0.dp, 0.dp)
                ),
            )
    )
}


