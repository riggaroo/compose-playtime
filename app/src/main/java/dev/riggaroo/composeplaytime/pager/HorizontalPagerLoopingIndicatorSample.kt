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
package dev.riggaroo.composeplaytime.pager

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay


@Preview
@Composable
fun HorizontalPagerLoopingIndicatorSample() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Display 10 items
            val pageCount = 10

            // We start the pager in the middle of the raw number of pages
            val loopingCount = Int.MAX_VALUE
            val startIndex = loopingCount / 2
            // Set the raw page count to a really large number
            val pagerState = rememberPagerState(initialPage = startIndex, pageCount = {loopingCount})

            fun pageMapper(index: Int): Int {
                return (index - startIndex).floorMod(pageCount)
            }

            HorizontalPager(
                state = pagerState,
                // Add 32.dp horizontal padding to 'center' the pages
                contentPadding = PaddingValues(horizontal = 32.dp),
                // Add some horizontal spacing between items
                pageSpacing = 4.dp,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { index ->
                // We calculate the page from the given index
                val page = pageMapper(index)
                PagerSampleItem(
                    page = page,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            val loopState = remember {
                mutableStateOf(true)
            }

            LoopControl(loopState, Modifier.align(Alignment.CenterHorizontally))

            ActionsRow(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                infiniteLoop = true
            )

            var underDragging by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = Unit) {
                pagerState.interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> underDragging = true
                        is PressInteraction.Release -> underDragging = false
                        is PressInteraction.Cancel -> underDragging = false
                        is DragInteraction.Start -> underDragging = true
                        is DragInteraction.Stop -> underDragging = false
                        is DragInteraction.Cancel -> underDragging = false
                    }
                }
            }

            val looping = loopState.value
            if (underDragging.not() && looping) {
                LaunchedEffect(key1 = underDragging) {
                    try {
                        while (true) {
                            delay(1000L)
                            val current = pagerState.currentPage
                            val currentPos = pageMapper(current)
                            val nextPage = current + 1
                            if (underDragging.not()) {
                                val toPage = nextPage.takeIf { nextPage < pageCount } ?: (currentPos + startIndex + 1)
                                if (toPage > current) {
                                    pagerState.animateScrollToPage(toPage)
                                } else {
                                    pagerState.scrollToPage(toPage)
                                }
                            }
                        }
                    } catch (e: CancellationException) {
                        Log.i("page", "Launched paging cancelled")
                    }
                }
            }
        }
    }
}

@Composable
fun LoopControl(
    loopState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = { loopState.value = loopState.value.not() },
        modifier = modifier
    ) {
        val icon = if (loopState.value) {
            Icons.Default.PauseCircle
        } else {
            Icons.Default.PlayCircle
        }
        Icon(imageVector = icon, contentDescription = null)
    }
}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}
