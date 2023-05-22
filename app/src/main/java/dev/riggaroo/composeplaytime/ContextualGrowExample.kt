package dev.riggaroo.composeplaytime

import androidx.annotation.Px
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cabin
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

val grayPlaceholderColor = Color(0xFFE0E0E0)

// An example of a composable growing into another composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Preview
@Composable
fun ContextualGrowExample() {
    var screen by remember {
        mutableStateOf(Screen.Home)
    }
    when (screen){
        Screen.Home -> {
            HomeScreen(onRentalClicked = {
                screen = Screen.Details
            })
        }
        Screen.Details -> {
            RentalDetails()
        }
    }

}
enum class Screen {
    Home,
    Details
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(onRentalClicked: () -> Unit) {
    var advancedSearch by remember {
        mutableStateOf(false)
    }
    val transition = updateTransition(targetState = advancedSearch, label = "")
    var clickedChip by remember {
        mutableStateOf(false)
    }
    var chipPosition by remember {
        mutableStateOf(Offset.Infinite)
    }
    var chipSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    var openFilterDialog by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Box(modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(grayPlaceholderColor)
            .clickable {
                advancedSearch = !advancedSearch
            }
        )

        val chipScrollableState = rememberScrollState()
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            // chips
            item {
                Row(modifier = Modifier
                    .wrapContentSize()
                    .horizontalScroll(chipScrollableState)) {
                    Spacer(modifier = Modifier.width(12.dp))
                    repeat(50) {
                        val locationOnScreen = remember {
                            mutableStateOf(Offset.Unspecified)
                        }
                        var size by remember {
                            mutableStateOf(IntSize.Zero)
                        }
                        val movableChip = remember {
                            movableContentOf {
                                Chip(onClick = {
                                    clickedChip = true
                                    chipPosition = locationOnScreen.value
                                    chipSize = size
                                }, modifier = Modifier.padding(4.dp)
                                    .onGloballyPositioned {
                                        locationOnScreen.value = it.positionInRoot()
                                        size = it.size
                                    }
                                ) {
                                    Text("                ")
                                }
                            }
                        }
                        movableChip()
                    }

                }
            }
            // grid
            items(200){
                RentalCard(modifier = Modifier.clickable {
                    onRentalClicked()
                })
            }
        }


        BottomBar()
    }
    if (openFilterDialog) {
        Dialog(onDismissRequest = {
            openFilterDialog = false
        }) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .fillMaxWidth()
                .height(300.dp)
            ) {

            }
        }
    }

    if (clickedChip) {
        val widthDp = with(LocalDensity.current) {
            chipSize.width.toDp()
        }
        val heightDp = with(LocalDensity.current) {
            chipSize.height.toDp()
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.graphicsLayer {
                translationX = chipPosition.x
                translationY = chipPosition.y
            }
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Red)
                .size(width = widthDp, height = heightDp)
                .padding(4.dp)
                ) {

            }
        }
    }
}

@Composable
private fun ChipOverlay(offset: Offset) {

}

@Composable
private fun BottomBar() {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            TabItem(Icons.Filled.Search, "Explore")
            TabItem(Icons.Filled.HeartBroken, "Wishlist")
            TabItem(Icons.Filled.Map, "Trips")
            TabItem(Icons.Filled.Message, "Inbox")
            TabItem(Icons.Filled.Person, "Profile")
        }
    }
}

@Composable
fun RentalCard(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        // image
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(grayPlaceholderColor)
            .aspectRatio(1f)
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // text
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(grayPlaceholderColor)
                .fillMaxWidth(0.5f)
                .height(24.dp))
            Box(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(grayPlaceholderColor)
                .fillMaxWidth(0.3f)
                .height(24.dp)
                )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(grayPlaceholderColor)
            .fillMaxWidth(0.3f)
            .height(24.dp))
    }
}
@Composable
fun TabItem(icon: ImageVector,
            text: String,
            modifier : Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center,
        modifier = modifier) {
        IconButton(onClick = { }) {
            Icon(icon, contentDescription = "", tint = Color.Gray)
        }
        Text(text, fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(CenterHorizontally))
    }
}
@Composable
fun DetailsScreen() {
    RentalDetails()
}
@Composable
fun RentalDetails(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)) {
        // image
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(grayPlaceholderColor)
            .aspectRatio(1f)
            .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // text
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(grayPlaceholderColor)
                .fillMaxWidth(0.5f)
                .height(24.dp))
            Box(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(grayPlaceholderColor)
                .fillMaxWidth(0.3f)
                .height(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(grayPlaceholderColor)
            .fillMaxWidth(0.3f)
            .height(24.dp))
    }
}