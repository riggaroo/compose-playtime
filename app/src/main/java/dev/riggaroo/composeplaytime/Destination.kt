package dev.riggaroo.composeplaytime

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
enum class Destination(val route: String) {
    Jellyfish("jellyfish"),
    HelloPath("hello_path"),
    BouncyRopes("bouncy_ropes"),
    BouncyLoader("bouncy_loader"),
    SmoothLineGraph("smooth_line_graph"),

    CenterSnappingPager("center_snapping_pager"),
    HorizontalPagerBasic("horizontal_pager_basic"),
    HorizontalPagerDifferentPaddings("horizontal_pager_different_paddings"),
    HorizontalPagerLoopingIndicator("horizontal_pager_looping_indicator"),
    HorizontalPagerLoopingTabs("horizontal_pager_looping_tabs"),
    HorizontalPagerScrollingContent("horizontal_pager_scrolling_content"),
    HorizontalPagerTabs("horizontal_pager_tabs_sample"),
    HorizontalPagerTransition("horizontal_pager_tabs_transition"),
    HorizontalPagerWithIndicator("horizontal_pager_tabs_with_indicator"),
    NestedPagesSample("nested_pages_sample"),
    VerticalPagerBasic("vertical_pager_basic"),
    VerticalPagerWithIndicator("vertical_pager_with_indicator"),

    PagerWithCubeTransition("pager_cube_out"),
    PagerWithCubeOutScalingTransition("pager_cube_out_scaling"),
    PagerWithCubeOutDepthTransition("pager_cube_out_depth"),
    PagerWithCubeInRotationTransition("pager_cube_in_rotation"),
    PagerWithCubeInScalingTransition("pager_cube_in_scaling_rotation"),
    PagerWithCubeInDepthTransition("pager_cube_in_depth"),
    PagerSpinningTransition("pager_spinning"),
    PagerDepthTransition("pager_depth"),
    PagerFadeOutTransition("pager_fade_transition"),
    PagerFanTransition("pager_fan_transition")
}