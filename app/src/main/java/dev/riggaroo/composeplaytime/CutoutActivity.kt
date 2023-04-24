package dev.riggaroo.composeplaytime

import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathUtils.flatten
import androidx.core.graphics.flatten
import androidx.core.view.WindowCompat
import kotlin.math.abs

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
@RequiresApi(Build.VERSION_CODES.S)
class CutoutActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT

        setContent {
            Box(modifier = Modifier.fillMaxSize()
                .windowInsetsPadding(WindowInsets.displayCutout)
                .onGloballyPositioned {
                    Log.d("position", "root coordinates: ${it.positionInRoot()}")
                    Log.d("position", "parent coordinates: ${it.positionInParent()}")
                    Log.d("position", "window coordinates: ${it.positionInWindow()}")
                }
            ){
                Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
                    Log.d("position", "box root coordinates: ${it.positionInRoot()}")
                    Log.d("position", "box parent coordinates: ${it.positionInParent()}")
                    Log.d("position", "box window coordinates: ${it.positionInWindow()}")
                })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun CutoutManual() {
    val displayCutout = LocalView.current.rootWindowInsets.displayCutout

    val cutoutPath = displayCutout?.cutoutPath
    val composePath = cutoutPath?.asComposePath()
    Log.d("TAG", "isCircle ${cutoutPath?.isCircle()}")
    if (composePath != null && cutoutPath.isCircle()) {
        DrawCutout(composePath)
    }
}
@Composable
private fun DrawCutout(displayCutout: Path) {

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val animatedPulse = infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse",
    )
    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        drawPath(path = displayCutout,
            color = Color.Green,
            style= Stroke(animatedPulse.value.dp.toPx())
        )
    })
}
fun android.graphics.Path.isCircle(threshold: Float = 1.0f): Boolean {
    val bounds = RectF()
    computeBounds(bounds, true)

    val x = bounds.centerX()
    val y = bounds.centerY()
    val r2 = sq(x - bounds.left)

    for (segment in flatten(error = threshold / 2.0f)) {
        val p1 = segment.start
        val p2 = segment.end

        if (abs(sq(p1.x - x) + sq(p1.y - y) - r2) > threshold ||
            abs(sq(p2.x - x) + sq(p2.y - y) - r2) > threshold) {
            return false
        }
    }


    return true
}

@Suppress("NOTHING_TO_INLINE")
inline fun sq(x: Float) = x * x

/*var pointerPosition by remember {
                    mutableStateOf(Offset(0f, 0f))
                }
                val insetCutoutModifier = Modifier.fillMaxSize()
                 //   .windowInsetsPadding(WindowInsets.displayCutout)
                    .pointerInput(PointerEventType.Move) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                event.changes.forEach {
                                    pointerPosition = it.position
                                    it.position.x
                                    it.consume()
                                }
                            }
                        }
                    }
                Canvas(modifier = insetCutoutModifier) {
                    this.drawCircle(Color.Red, radius = 20.dp.toPx(), center = pointerPosition)
                }*/