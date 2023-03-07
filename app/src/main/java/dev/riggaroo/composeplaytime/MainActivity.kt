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
import dev.riggaroo.composeplaytime.pager.HorizontalPagerDifferentPaddings
import dev.riggaroo.composeplaytime.pager.HorizontalPagerDifferentPaddingsSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerLoopingIndicatorSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerLoopingTabsSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerScrollingContentSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerWithIndicatorSample
import dev.riggaroo.composeplaytime.pager.HorizontalPagerWithOffsetTransition
import dev.riggaroo.composeplaytime.pager.HorizontalPagerWithOffsetTransitionSample
import dev.riggaroo.composeplaytime.pager.NestedPagersSample
import dev.riggaroo.composeplaytime.pager.VerticalPagerBasicSample
import dev.riggaroo.composeplaytime.pager.VerticalPagerWithIndicatorSample
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