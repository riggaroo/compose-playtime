/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("DEPRECATION")
package dev.riggaroo.composeplaytime.pager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberImagePainter
import dev.riggaroo.composeplaytime.R
import dev.riggaroo.composeplaytime.rememberRandomSampleImageUrl
import dev.riggaroo.composeplaytime.ui.theme.ComposePlaytimeTheme
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPagerWithOffsetTransitionSample() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.horiz_pager_with_transition_title)) },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        HorizontalPagerWithOffsetTransition(Modifier.padding(padding))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = 10,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = 32.dp),
        modifier = modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
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
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box {
                Image(
                    painter = rememberImagePainter(
                        data = rememberRandomSampleImageUrl(width = 600),
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )

                ProfilePicture(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        // We add an offset lambda, to apply a light parallax effect
                        .offset {
                            // Calculate the offset for the current page from the
                            // scroll position
                            val pageOffset =
                                ((pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction)
                            // Then use it as a multiplier to apply an offset
                            IntOffset(
                                x = (36.dp * pageOffset).roundToPx(),
                                y = 0
                            )
                        }
                )
            }
        }
    }
}

@Composable
private fun ProfilePicture(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.surface)
    ) {
        Image(
            painter = rememberImagePainter(rememberRandomSampleImageUrl()),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
    }
}
