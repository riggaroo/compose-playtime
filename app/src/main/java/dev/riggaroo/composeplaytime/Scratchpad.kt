package dev.riggaroo.composeplaytime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun BorderTest() {
    Box(
        modifier = Modifier
            .requiredSize(32.dp)
            .graphicsLayer {
                clip = true
                shape = CircleShape
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .border(width = 2.dp, color = Color.Black, shape = CircleShape)
            .background(Color.White)

    )
}

@Preview
@Composable
private fun Border2Test() {
    Box(
        modifier = Modifier
            .requiredSize(32.dp)
            .graphicsLayer {
                clip = true
                shape = CircleShape
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()

                drawCircle(Color.Black, style = Stroke(2.dp.toPx()))


            }
            //  .border(width = 2.dp, color = Color.Black, shape = CircleShape)
            //   .padding(2.dp)
            .background(Color.White)
    )
}


@Preview
@Composable
private fun BlendMode() {
    Box(modifier = Modifier.fillMaxSize()) {

        BlendModeContent(
            modifier = Modifier.size(100.dp).background(Color.Blue),
            blendMode = BlendMode.Difference,
            content = {
                Box(modifier = Modifier.fillMaxSize().background(Color.Red))
        })
        BlendModeContent(
            modifier = Modifier.size(100.dp).offset(50.dp, 50.dp).background(Color.Blue),
            blendMode = BlendMode.Multiply,
            content = {
                Box(modifier = Modifier.fillMaxSize().background(Color.Red))
            })
    }

}

@Composable
private fun BlendModeContent(
    modifier: Modifier = Modifier,
    blendMode: BlendMode,
    content: @Composable () -> Unit) {

    val graphicsLayer = rememberGraphicsLayer()
    Box(modifier = modifier
        .drawWithCache {
            graphicsLayer.buildLayer(this) {// ContentDrawScope
                drawContent()
            }
            onDrawWithContent {
                // Draw the content within the layer with the specified size
                graphicsLayer.blendMode = blendMode
                // Draw the layer into the original destination
                drawLayer(graphicsLayer)
            }
        }
    ) {
        content()
    }
}