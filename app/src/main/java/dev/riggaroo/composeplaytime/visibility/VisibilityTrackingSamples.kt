package dev.riggaroo.composeplaytime.visibility

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.riggaroo.composeplaytime.R
import dev.riggaroo.composeplaytime.pager.PagerSampleItem
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Scenarios:
 * Normal visibility ✅
 * Lazy list visibility ✅
 * Pager visibility ✅
 * Composables with Bottom sheets ✅
 * With navigation-compose ✅
 * Composables nested inside Activities ✅
 * Composables obscured by keyboard 🟠 -> depends on softInputMode in AndroidManifest.xml, if adjustResize, wont fire visible events when not visible.
 * Z-order obstruction ❌
 * Composables placed inside a Window ✅ -> Starts at window origin.
 */
@Preview
@Composable
private fun VisibleNormalScenario() {
    Box {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(30) { item ->
                Box(modifier = Modifier
                    .size(100.dp)
                    .background(randomColor())
                    .onVisibilityChanged { event ->
                        when (event) {
                            VisibleEvent.Invisible -> println("visibility: item $item - invisible")
                            is VisibleEvent.OnPositionChanged ->  println("visibility: item $item - position changed $event")
                            is VisibleEvent.Visible ->  println("visibility: item $item - visible $event")
                        }
                    }){
                    Text(item.toString(), fontSize = 22.sp)
                }
            }
        }
    }
}
fun randomColor(alpha : Int = 255) = Color(
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256),
    alpha = alpha)

/**
 * Unfortunately this solution doesn't work with drawing items on-top of others.
 */
@Preview
@Composable
private fun VisibleZOrder() {
    Box {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(30) {count ->
                Box(modifier = Modifier
                    .size(100.dp)
                    .background(randomColor())
                    .onVisibilityChanged {
                        println("bottom layer item visible event: $count, $it")
                    }){
                    Text(count.toString(), fontSize = 22.sp)
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .onVisibilityChanged {
                println("top layer item visible event: $it")
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun VisibilityPager() {
    val pagerState = rememberPagerState(pageCount = {10})

    // Display 10 items
    HorizontalPager(
        state = pagerState,
        // Add 32.dp horizontal padding to 'center' the pages
        contentPadding = PaddingValues(horizontal = 32.dp),
        // Add some horizontal spacing between items
        pageSpacing = 4.dp,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        PagerSampleItem(
            page = page,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .onVisibilityChanged {
                    println("Visible change $page: $it")
                }
        )
    }
}

@Preview
@Composable
private fun VisibleLazy() {
    Box {
        LazyColumn() {
            items(30) {count ->
                Box(modifier = Modifier
                    .size(100.dp)
                    .background(randomColor())
                    .onVisibilityChanged {
                        println("item: $count, $it")
                    }) {
                    Text(count.toString(), fontSize = 22.sp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun VisibleWindow() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        repeat(30) {count ->
            Box(modifier = Modifier
                .size(100.dp)
                .background(randomColor())
                .onVisibilityChanged {
                    println("bottom layer item visible event: $count, $it")
                }){
                Text(count.toString(), fontSize = 22.sp)
            }
        }
    }
}



@Preview
@Composable
private fun VisibleNavigationCompose() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .onVisibilityChanged {
                    println("visible profile $it")
                }
                .clickable {
                    navController.navigate("friendslist")
                }
            )
        }
        composable("friendslist") {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .onVisibilityChanged {
                    println("visible friendslist $it")
                }
                .clickable {
                    navController.navigate("profile")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun VisibleBottomSheet() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        repeat(30) {count ->
            Box(modifier = Modifier
                .size(100.dp)
                .background(randomColor())
                .onVisibilityChanged {
                    println("bottom layer item visible event: $count, $it")
                }
                .clickable {
                    showBottomSheet = true
                }){
                Text(count.toString(), fontSize = 22.sp, modifier = Modifier)
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.onVisibilityChanged {
                println("Visible Change bottom sheet: $it")
            },
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }) {
                Text("Hide bottom sheet", modifier = Modifier.onVisibilityChanged {
                    println("Visible event: Text Modal bottom sheet: $it")
                })
            }
        }
    }
}

@Preview
@Composable
private fun VisibleFragment() {

}

@Composable
fun VisibilityCustomView() {
    var selectedItem by remember { mutableIntStateOf(0) }
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates view
            TextView(context).apply {
                // Sets up listeners for View -> Compose communication
                setOnClickListener {
                    selectedItem = 1
                }
            }
        },
        update = { view ->
        }
    )
}

/**
 * This will pick up items underneath the keyboard and log them as visible
 *
 * The following three conditions need to be true in order for reporting to work excluding the keyboard:
 * - enableEdgeToEdge in the activity
 * - set android:softInputMode= adjustResize/adjustNothing
 * - Use inset padding modifiers on content, e.g. Modifier.safeDrawingPadding()
 */
@Preview
@Composable
fun VisibleKeyboardObstruction() {
    Column(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()) {
        var text by remember { mutableStateOf("Hello") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )

        Box {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                repeat(30) {count ->
                    Box(modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .background(Color.LightGray)
                        .onVisibilityChanged {
                            println("item: $count, $it")
                        }){
                        Text(count.toString(), fontSize = 22.sp)
                    }
                }
            }
        }
    }

}

class VisibilityTrackingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.interop_testing_visibility)

        val composeView = findViewById<ComposeView>(R.id.compose_view_visibility)
        composeView.setContent {
            VisibilityPager()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun VisibilityWindowCompose() {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.onVisibilityChanged {
                println("visible alert dialog $it")
            },
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Title", modifier = Modifier.onVisibilityChanged {
                    println("title alert dialog visible $it")
                })
            },
            text = {
                Text(text = "Turned on by default")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}
