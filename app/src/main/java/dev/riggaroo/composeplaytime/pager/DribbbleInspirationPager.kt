package dev.riggaroo.composeplaytime.pager

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import dev.riggaroo.composeplaytime.rememberRandomSampleImageUrl
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * https://dribbble.com/shots/17117814--Drag-This-audio-player-prototype
 */
@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DribbbleInspirationPager() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        val pagerState = rememberPagerState(pageCount = {10 })
        HorizontalPager(
            pageSpacing = 16.dp,
            beyondViewportPageCount = 2,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
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
fun SongInformationCard(
    pagerState: PagerState,
    page: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, ambientColor = Color.LightGray),
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier) {
            val pageOffset = ((pagerState.currentPage - page) + pagerState
                .currentPageOffsetFraction).absoluteValue
            Image(
                modifier = Modifier
                    .padding(32.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .graphicsLayer {
                        // get a scale value between 1 and 1.75f, 1.75 will be when its resting,
                        // 1f is the smallest it'll be when not the focused page
                        val scale = lerp(1f, 1.75f, pageOffset)
                        // apply the scale equally to both X and Y, to not distort the image
                        scaleX *= scale
                        scaleY *= scale
                    },
                painter = rememberAsyncImagePainter(
                    model = rememberRandomSampleImageUrl
                        (width = 1200)
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            SongDetails()
            DragToListen(pageOffset)
        }
    }
}

@Composable
private fun DragToListen(pageOffset: Float) {
    Box(
        modifier = Modifier
            .height(150.dp * (1 - pageOffset))
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - pageOffset
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Rounded.MusicNote, contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
            )
            Text("DRAG TO LISTEN")
            Spacer(modifier = Modifier.size(4.dp))
            DragArea()
        }
    }
}

@Composable
private fun DragArea() {
    Box {
        Canvas(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
        ) {
            val sizeGap = 16.dp.toPx()
            val numberDotsHorizontal = size.width / sizeGap + 1
            val numberDotsVertical = size.height / sizeGap + 1
            repeat(numberDotsHorizontal.roundToInt()) { horizontal ->
                repeat(numberDotsVertical.roundToInt()) { vertical ->
                    drawCircle(
                        Color.LightGray.copy(alpha = 0.5f), radius = 2.dp.toPx
                            (), center =
                        Offset(horizontal * sizeGap + sizeGap, vertical * sizeGap + sizeGap)
                    )
                }
            }
        }
        Icon(
            Icons.Rounded.ExpandMore, "down",
            modifier = Modifier
                .size(height = 24.dp, width = 48.dp)
                .align(Alignment.Center)
                .background(Color.White)
        )
    }
}

@Composable
private fun SongDetails() {
    Spacer(modifier = Modifier.padding(8.dp))
    Text(
        "Artist",
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(4.dp))
    Text(
        "A Song Title",
        fontSize = 24.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(8.dp))
    StarRating(3)
}

@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val yellowColor = Color(0xFFFF9800)
        for (i in 0 until stars) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "Star",
                tint = yellowColor,
                modifier = Modifier.size(36.dp)
            )
        }
        for (i in 0 until (5 - stars)) {
            Icon(
                Icons.Rounded.Star, contentDescription = "Star empty",
                modifier = Modifier.size(36.dp),
                tint = yellowColor.copy(alpha = 0.25f)
            )
        }
    }
}
