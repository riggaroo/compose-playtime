package dev.riggaroo.composeplaytime.pager

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.riggaroo.composeplaytime.R
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CenterSnapPager() {
    val pagerState = rememberPagerState(pageCount = {
        listPageItem.size
    })
    val coroutineScope = rememberCoroutineScope()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray))
        val pageSize = 70.dp
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(pageSize = pageSize),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            contentPadding = PaddingValues(
                start = (maxWidth - pageSize) / 2,
                end = (maxWidth - pageSize) / 2
            ),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(30),
                snapAnimationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
        ) { page ->

            CircleFilterItem(filter = listPageItem[page],
                pagerState = pagerState,
                page = page,
                onPageSelected = { filter ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircleFilterItem(
    filter: Filter,
    pagerState: PagerState,
    page: Int,
    onPageSelected: (Filter) -> Unit,
) {
    Column(modifier = Modifier
        .clickable {
            onPageSelected(filter)
        }
        .graphicsLayer {
            // Calculate the absolute offset for the current page from the
            // scroll position. We use the absolute value which allows us to mirror
            // any effects for both directions
            val pageOffset = ((pagerState.currentPage - page) + pagerState
                .currentPageOffsetFraction).absoluteValue


            // We animate the scaleX + scaleY, between 85% and 100%
            lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            ).also { scale ->
                scaleX = scale
                scaleY = scale
            }

            // We animate the alpha, between 50% and 100%
            alpha = lerp(
                start = 0.25f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            if (pagerState.currentPage == page) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
            Image(
                painter = painterResource(id = filter.imagePreview),
                contentDescription = filter.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
        }
        Text(
            filter.name,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

val listPageItem = listOf(
    Filter("Black & White", R.drawable.dog),
    Filter("Cinematic", R.drawable.dog),
    Filter("Juno", R.drawable.sunset),
    Filter("Dazzle", R.drawable.dog),
    Filter("Shiny", R.drawable.dog),
    Filter("Fun", R.drawable.sunset),
    Filter("Playful", R.drawable.dog),
    Filter("Romantic", R.drawable.dog),
    Filter("OTT", R.drawable.sunset),
    Filter("Space", R.drawable.dog),
    Filter("Paris", R.drawable.dog),
    Filter("Chicago", R.drawable.sunset),
)

data class Filter(
    val name: String,
    @DrawableRes val imagePreview: Int,
)