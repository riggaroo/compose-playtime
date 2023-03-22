package dev.riggaroo.composeplaytime.pager.transformations

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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.riggaroo.composeplaytime.rememberRandomSampleImageUrl
import kotlin.math.absoluteValue

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithFanTransition(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = 10,
        modifier = modifier.fillMaxSize(),
        state = pagerState,
        beyondBoundsPageCount = 2
    ) { page ->
        Box(Modifier
            .pagerFanTransition(page, pagerState)
            .fillMaxSize()) {
            Image(
                painter = rememberAsyncImagePainter(model = rememberRandomSampleImageUrl
                    (width = 1200)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerFanTransition(page: Int, pagerState: PagerState) : Modifier {
    return this.then(graphicsLayer {
        cameraDistance = 2000f
        // Calculate the absolute offset for the current page from the
        // scroll position.
        val pageOffset = ((pagerState.currentPage - page) + pagerState
            .currentPageOffsetFraction)
        translationX = pageOffset * size.width
        transformOrigin = TransformOrigin(0f, 0.5f)

        if (pageOffset < -1f){
            // page is far off screen
            alpha = 0f
        } else if (pageOffset <= 0){
            // page is to the right of the selected page or the selected page
            alpha = 1f
            rotationY = -120f * pageOffset.absoluteValue

        } else if (pageOffset <= 1){
            // page is to the left of the selected page
            alpha = 1f
            rotationY = 120f * pageOffset.absoluteValue
        } else {
            alpha = 0f
        }
    })
}