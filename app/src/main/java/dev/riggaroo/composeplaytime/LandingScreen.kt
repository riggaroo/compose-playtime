package dev.riggaroo.composeplaytime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.riggaroo.composeplaytime.ui.theme.ComposePlaytimeTheme

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navigate: (Destination) -> Unit) {
    ComposePlaytimeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Text("Animation Examples")
                        },
                        scrollBehavior = scrollBehavior
                    )
                }) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    itemsIndexed(Destination.values()) { index, item ->
                        Text(
                            index.toString() + " " + item.name,
                            modifier = Modifier
                                .clickable {
                                    navigate(item)
                                }
                                .defaultMinSize(minHeight = 48.dp)
                                .padding(8.dp)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}