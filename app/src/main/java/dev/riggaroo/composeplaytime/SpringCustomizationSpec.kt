package dev.riggaroo.composeplaytime

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun SpringConfig() {
    var state by remember {
        mutableStateOf("A")
    }
    val overallSpring = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    LaunchedEffect(key1 = "hi", block = {
        overallSpring.animateTo(1f, animationSpec = spring()) {
            Log.d("tag", "animation value ${this.value}, targetValue: ${this.targetValue}")
        }
    })

    val transition = updateTransition(targetState = state, label = "A")
   /* val alphaA = transition.animateFloat(label = "AlphaA") {
        when (it){
            "A" -> 1f
                "B" -> 0f
            else -> 0f
        }
    }
    val alphaB = transition.animateFloat(label = "alphaB")  {
        when (it){
            "A" -> 0f
            "B" -> 1f
            else -> 0f
        }
    }*/
    when (state){
        "A" -> Screen1 {
            state = "B"
        }
        "B" -> Screen2 {
            state = "A"
        }
    }
    /**
     * position-x slide 100pt: 0% - 100%
     * source screen fades-out: 0% - 15%
     * destination screen fades-in: 15% - 100%
     */
    /*AnimatedContent(targetState = state, label = "",
        transitionSpec = {
                fadeIn() +
                        slideInHorizontally(
                            animationSpec = tween(400),
                            initialOffsetX = { fullWidth -> fullWidth }) with
                        fadeOut(animationSpec = tween(200, delayMillis = 100))
        }
        ) {*/

    /*}*/
}

@Composable
private fun Screen1(onClick : ()-> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Red)
        .clickable { onClick() }){

    }
}
@Composable
private fun Screen2(onClick : ()-> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)
        .clickable { onClick() }){}
}
