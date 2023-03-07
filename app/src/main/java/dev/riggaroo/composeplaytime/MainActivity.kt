package dev.riggaroo.composeplaytime

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.riggaroo.composeplaytime.pager.CenterSnapPager
import dev.riggaroo.composeplaytime.pager.HorizontalPagerTabsSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerBasicSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerDifferentPaddingsSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerLoopingIndicatorSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerLoopingTabsSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerScrollingContentSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerWithIndicatorSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerWithOffsetTransitionSample
import dev.riggaroo.composeplaytime.pager.NestedPagersSample
import dev.riggaroo.composeplaytime.pager.VerticalPagerBasicSample
import dev.riggaroo.composeplaytime.pager.VerticalPagerWithIndicatorSample
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeInDepthTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeInScalingTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeInTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeOutDepthTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeOutScalingTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithCubeOutTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithDepthTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithFadeTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithFanTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithFidgetSpinningTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithGateTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithHingeTransition
import dev.riggaroo.composeplaytime.pager.transformations.HorizontalPagerWithSpinningTransition
import dev.riggaroo.composeplaytime.ui.theme.ComposePlaytimeTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaytimeTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") {
                        LandingScreen(navigate = {
                            navController.navigate(it.route)
                        })
                    }
                    Destination.values().forEach { destination ->
                        composable(destination.route) {
                            when (destination) {
                                Destination.BouncyLoader ->
                                    BouncyLoader()
                                Destination.Jellyfish -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        JellyfishAnimation()
                                    }
                                }
                                Destination.HelloPath -> {
                                    GradientAlongPathAnimation()
                                }

                                Destination.BouncyRopes -> BouncyRopes()
                                Destination.SmoothLineGraph -> SmoothLineGraph()

                                Destination.CenterSnappingPager -> CenterSnapPager()
                                Destination.HorizontalPagerBasic -> HorizontalPagerBasicSample()
                                Destination.HorizontalPagerDifferentPaddings -> HorizontalPagerDifferentPaddingsSample()
                                Destination.HorizontalPagerLoopingIndicator ->
                                    HorizontalPagerLoopingIndicatorSample()
                                Destination.HorizontalPagerLoopingTabs ->
                                    HorizontalPagerLoopingTabsSample()
                                Destination.HorizontalPagerScrollingContent ->
                                    HorizontalPagerScrollingContentSample()
                                Destination.HorizontalPagerTabs -> HorizontalPagerTabsSample()
                                Destination.HorizontalPagerTransition -> HorizontalPagerWithOffsetTransitionSample()
                                Destination.HorizontalPagerWithIndicator ->
                                    HorizontalPagerWithIndicatorSample()
                                Destination.NestedPagesSample -> NestedPagersSample()
                                Destination.VerticalPagerBasic -> VerticalPagerBasicSample()
                                Destination.VerticalPagerWithIndicator ->
                                    VerticalPagerWithIndicatorSample()
                                Destination.PagerWithCubeTransition ->
                                    HorizontalPagerWithCubeOutTransition()
                                Destination.PagerWithCubeOutScalingTransition ->
                                    HorizontalPagerWithCubeOutScalingTransition()
                                Destination.PagerWithCubeOutDepthTransition ->
                                    HorizontalPagerWithCubeOutDepthTransition()
                                Destination.PagerWithCubeInRotationTransition ->
                                    HorizontalPagerWithCubeInTransition()
                                Destination.PagerWithCubeInScalingTransition ->
                                    HorizontalPagerWithCubeInScalingTransition()
                                Destination.PagerWithCubeInDepthTransition ->
                                    HorizontalPagerWithCubeInDepthTransition()
                                Destination.PagerSpinningTransition ->
                                    HorizontalPagerWithSpinningTransition()
                                Destination.PagerDepthTransition ->
                                    // todo
                                    HorizontalPagerWithDepthTransition()
                                Destination.PagerFadeOutTransition ->
                                    HorizontalPagerWithFadeTransition()
                                Destination.PagerFanTransition ->
                                    // todo
                                    HorizontalPagerWithFanTransition()
                                Destination.PagerFidgetTransition ->
                                    HorizontalPagerWithFidgetSpinningTransition()
                                Destination.PagerHingeTransition ->
                                    HorizontalPagerWithHingeTransition()
                                Destination.PagerGateTransition ->
                                    HorizontalPagerWithGateTransition()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposePlaytimeTheme {
        Greeting("Android")
    }
}