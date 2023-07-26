package dev.riggaroo.composeplaytime.pager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.riggaroo.composeplaytime.pager.transformations.pagerFadeTransition

/**
 * https://dribbble.com/shots/4718271-Areia-Dots-Interaction-Free-prd-6
 */
@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LineIndicatorExample() {
    Box(modifier = Modifier.fillMaxSize()) {
        val pageCount = 5
        val pagerState = rememberPagerState(pageCount = {pageCount})
        val destinations = listOf("Maldives", "Cape Town", "London", "Greece", "New York")
        HorizontalPager(
            beyondBoundsPageCount = 2,
            state = pagerState
        ) { page ->
            Box(modifier = Modifier.pagerFadeTransition(page, pagerState = pagerState)) {
                PagerSampleItem(
                    page = page
                )
                CircularProgressIndicator()
                AnimatedVisibility(
                    pagerState.settledPage == page,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = 16.dp,
                            bottom = 32.dp
                        )
                ) {
                    Text(
                        destinations[page],
                        fontSize = 48.sp,
                        color = Color.White
                    )
                }
            }
        }
        Row(
            Modifier
                .height(24.dp)
                .padding(start = 4.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(pageCount) { iteration ->
                val lineWeight = animateFloatAsState(
                    targetValue = if (pagerState.currentPage == iteration) {
                        1.5f
                    } else {
                        if (iteration < pagerState.currentPage) {
                            0.5f
                        } else {
                            1f
                        }
                    }, label = "size", animationSpec = tween(300, easing = EaseInOut)
                )
                val color =
                    if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                        .weight(lineWeight.value)
                        .height(4.dp)
                )
            }
        }
    }
}

