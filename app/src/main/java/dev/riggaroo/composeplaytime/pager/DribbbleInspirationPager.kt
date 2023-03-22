package dev.riggaroo.composeplaytime.pager

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import dev.riggaroo.composeplaytime.rememberRandomSampleImageUrl
import kotlin.math.absoluteValue

/**
 * https://dribbble.com/shots/17117814--Drag-This-audio-player-prototype
 */
@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DribbbleInspirationPager() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFECECEC))) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            pageCount = 10,
            pageSpacing = 16.dp,
            beyondBoundsPageCount = 2,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState,
                lowVelocityAnimationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessHigh),
                snapAnimationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium),
                highVelocityAnimationSpec = rememberSplineBasedDecay())
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                SongInformationCard(
                    modifier = Modifier
                        .padding(32.dp)
                        .align(Alignment.Center),
                    pagerState = pagerState,
                    page = page
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongInformationCard(modifier: Modifier = Modifier, pagerState: PagerState, page: Int) {
    var dragAmount by remember {
        mutableStateOf(0f)
    }
    val draggableState = rememberDraggableState(onDelta = { dragAmount += it})
    Card(modifier = modifier
        .draggable(draggableState, orientation = Orientation.Vertical)
        .graphicsLayer {
            translationY = dragAmount
        }
        .fillMaxWidth()
        .wrapContentHeight()
        .shadow(16.dp, ambientColor = Color.LightGray),
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = Color.White)
        ) {
        Column(modifier = Modifier.padding(32.dp)) {
            val pageOffset = ((pagerState.currentPage - page) + pagerState
                .currentPageOffsetFraction).absoluteValue
            Log.d("TAG", "pageoffset $pageOffset, $page")
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .graphicsLayer {
                        val lerpedOffset = lerp(1f, 1.75f, 1 - pageOffset)
                        scaleX *= lerpedOffset
                        scaleY *= lerpedOffset
                    },
                painter = rememberAsyncImagePainter(
                    model = rememberRandomSampleImageUrl
                        (width = 1200)
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Song Image"
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Text("Artist",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(4.dp))
            Text("A Song Title",
                fontSize = 22.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(8.dp))
            StarRating(3)

            Log.d("tag","currentPageOffset $pageOffset")
            Column(){
                Spacer(modifier = Modifier
                    .height(100.dp * (1 - pageOffset))
                    .fillMaxWidth())
            }
        }
    }
}

@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        for (i in 0 until stars) {
            Icon(Icons.Rounded.Star,
                contentDescription = "Star",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(36.dp)
                )
        }
        for (i in 0 until (5-stars)){
            Icon(Icons.Rounded.StarBorder, contentDescription = "Star empty",
                modifier = Modifier.size(36.dp)
                )
        }
    }
}

@Composable
fun DragToListen() {

}