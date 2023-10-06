package dev.riggaroo.composeplaytime

import android.widget.TextView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlin.math.roundToInt

@Preview
@Composable
fun BoxMovementDemo() {
    var move by remember {
        mutableStateOf(false)
    }
    val animateY = animateDpAsState(
        targetValue = if (move) {
            50.dp
        } else {
            0.dp
        }, label = "y",
        animationSpec = tween(3000, easing = LinearEasing)
    )
    val animateYNonLinear = animateDpAsState(
        targetValue = if (move) {
            200.dp
        } else {
            0.dp
        }, label = "y",
        animationSpec = tween(3000, easing = FastOutSlowInEasing)
    )
    Row(modifier = Modifier
        .fillMaxSize()
        .clickable {
            move = !move
        }
        .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .offset(y = animateY.value)
                .size(100.dp)
                .background(Color(0xff01E489))

        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .offset(y = animateYNonLinear.value)
                .size(100.dp)
                .background(Color(0xff01E489))
        )
    }
}

@Preview
@Composable
fun AnimateAround() {
    Box(modifier = Modifier.padding(16.dp)) {
        with(LocalDensity.current) {
            var position by remember {
                mutableStateOf(IntOffset.Zero)
            }
            val positionAnimated = animateIntOffsetAsState(
                targetValue = position, label = "position",
                animationSpec = spring(stiffness = 50f, dampingRatio = dampingRatio(20f, 50f))
                /*  tween(1000, easing = LinearEasing)*/
            )
            val sizeRem = remember {
                IntOffset(25.dp.toPx().toInt(), 25.dp.toPx().toInt())
            }
            val path = remember {
                Path().apply {
                    moveTo(position.x.toFloat()+ sizeRem.x, position.y.toFloat()+ sizeRem.y)
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        position = IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                    }
                }
                .padding(16.dp)
            ) {
                path.lineTo(
                    (positionAnimated.value.x + sizeRem.x).toFloat(),
                    (positionAnimated.value.y + sizeRem.y).toFloat()
                )
                Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                    drawPath(
                        path,
                        Color.DarkGray,
                        style = Stroke(
                            width = 6f,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10f, 20f),
                                phase = 25f
                            )
                        )
                    )
                })
                Box(modifier = Modifier
                    .offset { positionAnimated.value - sizeRem }
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xff01E489))
                )
            }
        }
    }
}

@Preview
@Composable
fun SpringConfigMaker() {
    val path by remember {
        mutableStateOf(Path())
    }
    val startTime = remember {
        System.currentTimeMillis()
    }
    var stiffness by remember {
        mutableFloatStateOf(Spring.StiffnessLow)
    }
    var dampingRatio by remember {
        mutableFloatStateOf(Spring.DampingRatioNoBouncy)
    }
    var animation = remember {
        Animatable(0f)
    }
    var areaSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    val pathInfo = remember(dampingRatio, stiffness) {
        val springFunction = FloatSpringSpec(dampingRatio, stiffness)
        val pathNew = Path()
        pathNew.moveTo(0f, 0f)
        repeat(1500){
            val result = springFunction.getValueFromNanos(it.toLong(), 0f, 1f, 0f)
            pathNew.lineTo(it.toFloat(), result)
        }
        pathNew
    }


    LaunchedEffect(key1 = stiffness, key2 = dampingRatio, key3 = areaSize, block = {
        animation.snapTo(0f)
        /*animation.animateTo(areaSize.height / 2f, animationSpec = spring(dampingRatio, stiffness)) {
            path.lineTo((System.currentTimeMillis() - startTime).toFloat(), this.value)
        }*/
    })

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .onSizeChanged {
                areaSize = it
            }, onDraw = {
            scale( size.width,  size.height){
                drawPath(pathInfo, Color.Black, style = Stroke(3f))
            }
          //  drawCircle(Color(0xff01E489), radius = 25.dp.toPx(), center = animation.value)
        })
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Slider(
                value = stiffness,
                onValueChange = { stiffness = it },
                valueRange = 50f..1500f
            )
            Text(text = "Stiffness: $stiffness")
            Slider(
                value = dampingRatio,
                onValueChange = { dampingRatio = it },
                valueRange = 0f..1.0f
            )
            Text(text = "dampingRatio: $dampingRatio")
        }
    }



}

@Preview
@Composable
fun VisualizeSpringConfigs() {
    var clicked by remember {
        mutableStateOf(false)
    }
    val target = if (clicked){
        500f
    } else {
        0f
    }
   /* val gentleSpring = animateFloatAsState(targetValue = target, animationSpec = gentleSpring(), label = "gentle")
    val quickSpring = animateFloatAsState(targetValue = target, animationSpec = quickSpring(), label = "gentle")
    val bouncySpring = animateFloatAsState(targetValue = target, animationSpec = bouncySpring(), label = "gentle")
    val slowSpring = animateFloatAsState(targetValue = target, animationSpec = slowSpring(), label = "gentle")*/

 /*   val gentleSpring = animateFloatAsState(targetValue = target, animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy), label = "gentle")
    val quickSpring = animateFloatAsState(targetValue = target, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy), label = "gentle")
    val bouncySpring = animateFloatAsState(targetValue = target, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "gentle")
    val slowSpring = animateFloatAsState(targetValue = target, animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy), label = "gentle")
*/
    val gentleSpring = animateFloatAsState(targetValue = target, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "gentle")
    val quickSpring = animateFloatAsState(targetValue = target, animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "gentle")
    val bouncySpring = animateFloatAsState(targetValue = target, animationSpec = spring(stiffness = Spring.StiffnessMedium), label = "gentle")
    val slowSpring = animateFloatAsState(targetValue = target, animationSpec = spring(stiffness = Spring.StiffnessHigh), label = "gentle")

    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {
                clicked = !clicked
            }
        }
        .padding(16.dp)
    ) {
        Box(modifier = Modifier
            .size(50.dp)
            .offset(x = gentleSpring.value.dp)
            .clip(CircleShape)
            .background(Color(0xff01E489))
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier
            .size(50.dp)
            .offset(x = quickSpring.value.dp)
            .clip(CircleShape)
            .background(Color(0xff01E489))
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier
            .size(50.dp)
            .offset(x = bouncySpring.value.dp)
            .clip(CircleShape)
            .background(Color(0xff01E489))
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier
            .size(50.dp)
            .offset(x = slowSpring.value.dp)
            .clip(CircleShape)
            .background(Color(0xff01E489))
        )
    }
}

fun dampingRatio(damping: Float, stiffness: Float) = damping / (2f * kotlin.math.sqrt(stiffness))
fun <T> gentleSpring() = spring<T>(stiffness = 100f, dampingRatio = dampingRatio(15f, 100f))
fun <T> quickSpring() = spring<T>(stiffness = 300f, dampingRatio = dampingRatio(20f, 300f))
fun <T> bouncySpring() = spring<T>(stiffness = 600f, dampingRatio = dampingRatio(15f, 600f))
fun <T> slowSpring() = spring<T>(stiffness = 80f, dampingRatio = dampingRatio(20f, 80f))

