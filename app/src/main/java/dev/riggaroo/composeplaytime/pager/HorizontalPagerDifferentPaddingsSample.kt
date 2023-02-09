/*
 * Copyright 2022 The Android Open Source Project
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

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import dev.riggaroo.composeplaytime.R
import dev.riggaroo.composeplaytime.rememberRandomSampleImageUrl
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")// Sample deals with paddings itself
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HorizontalPagerDifferentPaddingsSample() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.horiz_pager_title_different_paddings)) },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            HorizontalPagerDifferentPaddings()
        }

    }
}

/**
 * This demo demonstrates how to achieve a behavior where for the user it feels like
 * there is no start padding before the first item and no end padding after the last item,
 * but all other items are centered with half of the padding applied for the first/last item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerDifferentPaddings() {
    val count = 4
    val padding = 16.dp
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = count,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = padding),
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Card(
            Modifier
                .offset {
                    // Calculate the offset do neutralize paddings on the sides on
                    // the first and the last page.
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    val offsetToFillStartPadding = minOf(page + pageOffset - 1, 0f)
                    val offsetToFillEndPadding = maxOf(page + pageOffset - count + 2, 0f)
                    val xOffset =
                        padding.toPx() * (offsetToFillStartPadding + offsetToFillEndPadding)
                    IntOffset(x = xOffset.roundToInt(), y = 0)
                }
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(18.dp)
        ) {
            Image(
                painter = rememberImagePainter(
                    data = rememberRandomSampleImageUrl(width = 600),
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
