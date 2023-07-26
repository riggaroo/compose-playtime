package dev.riggaroo.composeplaytime.flowlayout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun FlowWithOverflow() {
    var itemHeight by remember {
        mutableIntStateOf(0)
    }
    var shouldShowMoreButton by remember {
        mutableStateOf(false)
    }
    var constrainHeight by remember {
        mutableStateOf(true)
    }
    val numberRowsToConstrain = 2
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.border(1.dp, Color.Black)
    ) {
        FlowRow(modifier = Modifier
            .graphicsLayer { clip = true }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val constrainedHeight =
                    if (constrainHeight) itemHeight * numberRowsToConstrain else placeable.height
                shouldShowMoreButton = placeable.height > constrainedHeight
                val maxHeight = if (shouldShowMoreButton) constrainedHeight else placeable.height
                layout(placeable.width, maxHeight) {
                    placeable.placeRelative(0, 0)
                }
            }) {
            ChipItem("Price: High to Low", modifier = Modifier.onSizeChanged {
                itemHeight = it.height
            })
            ChipItem("Avg rating: 4+")
            ChipItem("Free breakfast")
            ChipItem("Free cancellation")
            ChipItem("£50 pn")
            ChipItem("Price: High to Low")
            ChipItem("Avg rating: 4+")
            ChipItem("Free breakfast")
            ChipItem("Free cancellation")
            ChipItem("£50 pn")
            ChipItem("Price: High to Low")
            ChipItem("Avg rating: 4+")
            ChipItem("Free breakfast")
            ChipItem("Free cancellation")
            ChipItem("£50 pn")
            ChipItem("Price: High to Low")
            ChipItem("Avg rating: 4+")
            ChipItem("Free breakfast")
            ChipItem("Free cancellation")
            ChipItem("£50 pn")
        }
        if (shouldShowMoreButton || !constrainHeight) {
            TextButton(onClick = {
                constrainHeight = !constrainHeight
            }) {
                Text(
                    if (constrainHeight) {
                        "More"
                    } else {
                        "Less"
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Chip(
        modifier = modifier.padding(end = 4.dp),
        onClick = {},
        leadingIcon = {},
        border = BorderStroke(1.dp, Color(0xFF3B3A3C))
    ) {
        Text(text)
    }
}