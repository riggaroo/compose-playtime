package dev.riggaroo.composeplaytime

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star

@Preview
@Composable
private fun BasicClip() {
    Box(modifier = Modifier
        .clip(CircleShape)
        .size(200.dp)){
        Image(painter = painterResource(id = R.drawable.dog),
            contentDescription = "dog",
            contentScale = ContentScale.Crop)
    }
}

@Preview
@Composable
private fun AdvancedClipShape() {
    val scallopShape = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            rounding = CornerRounding(0.5f),
            innerRounding = CornerRounding(0f)
        )
    }
    Box(modifier = Modifier
        .clip(RoundedPolygonShape(scallopShape))
        .size(200.dp)){
        
        Image(painter = painterResource(id = R.drawable.dog),
            contentDescription = "dog",
            contentScale = ContentScale.Crop)
    }
}

@Preview
@Composable
private fun AdvancedClipRotating() {
    val scallopShape = remember {
        RoundedPolygonShape(RoundedPolygon.star(
            numVerticesPerRadius = 12,
            rounding = CornerRounding(0.5f),
            innerRounding = CornerRounding(0f)
        ))
    }
    Box(modifier = Modifier
        .clip(scallopShape)
        .size(200.dp)
    ){
        Image(painter = painterResource(id = R.drawable.dog),
            contentDescription = "dog",
            contentScale = ContentScale.Crop)
    }
}

@Preview
@Composable
fun StackedAvatars(modifier: Modifier = Modifier) {
    val size = 80.dp
    val sizeModifier = Modifier.size(size)
    val avatars = listOf(R.drawable.dog, R.drawable.sunset,R.drawable.dog, R.drawable.sunset, R.drawable.dog)
    val width = (size / 2) * (avatars.size + 1)
    Box(modifier = modifier
        .size(width, size)
        .graphicsLayer {
            // Use an offscreen buffer as underdraw protection when using blendmodes that clear destination pixels
            compositingStrategy = CompositingStrategy.Offscreen
        },
    ) {
        var offset = 0.dp
        for (avatar in avatars) {
            Avatar(strokeWidth = 10.0f,
                modifier = sizeModifier.offset(offset)) {
                Image(
                    painter = painterResource(id = avatar),
                    contentScale = ContentScale.Crop,
                    contentDescription = null)
            }
            offset += size / 2
        }
    }
}

@Composable
fun Avatar(
    strokeWidth: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val stroke = remember(strokeWidth) {
        Stroke(width = strokeWidth)
    }
    Box(modifier = modifier
        .drawWithContent {
            drawContent()
            drawCircle(
                Color.Black,
                size.minDimension / 2,
                size.center,
                style = stroke,
                blendMode = BlendMode.Clear
            )
        }
        .graphicsLayer {
            clip = true
            shape = CircleShape
        }
    ) {
        content()
    }
}

@Preview
@Composable
fun AvatarStackDemo() {
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize()) {
        val backgroundGradient = remember {
            Brush.linearGradient(
                0.0f to Color.Red,
                1.0f to Color.Blue
            )
        }
        StackedAvatars(Modifier.background(backgroundGradient))
    }
}

@Preview
@Composable
private fun BlurTextInvert() {
    Box {
        Image(
            painter = painterResource(id = R.drawable.cape_town),
            contentDescription = "",
            modifier = Modifier
        )
        val graphicsLayer = rememberGraphicsLayer()
        Box(modifier = Modifier.graphicsLayer {
            compositingStrategy = CompositingStrategy.Offscreen
        }) {
            Image(
                painter = painterResource(id = R.drawable.cape_town),
                contentDescription = "",
                modifier = Modifier.blur(20.dp)
            )
            Text(text = "CAPE TOWN",
                fontSize = 96.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp)
                    .align(Alignment.BottomCenter)
                    .blendMode(graphicsLayer, BlendMode.DstOut)
            )
        }
    }
}

fun Modifier.blendMode(graphicsLayer: GraphicsLayer, blendMode: BlendMode): Modifier {
    return this.drawWithCache {
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.blendMode = blendMode
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}
