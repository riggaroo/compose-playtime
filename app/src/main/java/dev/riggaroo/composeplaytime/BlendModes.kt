package dev.riggaroo.composeplaytime

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.riggaroo.composeplaytime.pager.PagerSampleItem
import kotlin.math.max
import kotlin.math.min

@Preview
@Composable
fun BlendModesExamples() {
    Box() {
        val startIndex = Int.MAX_VALUE / 2
        val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { Int.MAX_VALUE })
        val graphicsLayer = rememberGraphicsLayer()
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .colorFilter(ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(0f)
                }))
        ) { index ->
            // We calculate the page from the given index
            val page = (index - startIndex).floorMod(pagerState.pageCount)

            PagerSampleItem(
                page = page,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
        val textGraphicsLayer = rememberGraphicsLayer()
        Text("breaking news",
            color = Color.White,
            fontSize = 140.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .align(Alignment.Center)
                .blendMode(BlendMode.Difference))
    }
}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}

fun Modifier.blendMode(blendMode: BlendMode): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
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

private fun Modifier.colorFilter(colorFilter: ColorFilter): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.colorFilter = colorFilter
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}

@Preview
@Composable
fun InvertedColorDemo() {
    val infiniteTransition = rememberInfiniteTransition()
    val centerX = infiniteTransition.animateFloat(
        .1f,
        0.9f,
        infiniteRepeatable(tween(3000), RepeatMode.Reverse)
    )
    val graphicsLayerBlue = rememberGraphicsLayer()
    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .size(200.dp, 120.dp)
        .background(Color.DarkGray, CircleShape)
       /* .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)*/
    ) {
        Text(
            "Inverted Colors",
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(start = 30.dp)
                .align(Alignment.Center)
        )

        Box(modifier = Modifier.fillMaxSize().drawBehind {
            val radius = size.minDimension / 4
            drawCircle(
                Color.Cyan,
                radius = radius,
                center = Offset(
                    max(radius, min(size.width * centerX.value, size.width - radius)),
                    size.height / 2
                ),
            )
        }.blendMode( blendMode = BlendMode.Xor))
    }
}
