package dev.riggaroo.composeplaytime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * To test:
 * - Normal composables
 * - Composables drawn on top of other composables
 * - Composables that are lazy
 * - Composables that are in a Pager
 * - Composables placed inside a Window
 * - Composables in a Fragment
 * - Composables obscured by keyboard
 * - with navigation-compose
 */
@Preview
@Composable
private fun VisibleNormalScenario() {
    Box {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(30) {count ->
                Box(modifier = Modifier
                    .size(100.dp)
                    .background(randomColor())
                    .onVisibleChange {
                        println("item: $count, ${it.isVisible}, $it")
                    }){
                    Text(count.toString(), fontSize = 22.sp)
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

@Preview
@Composable
private fun VisibleZOrder() {

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
                    .onVisibleChange {
                        println("item: $count, ${it.isVisible}, $it")
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

}

@Preview
@Composable
private fun VisibleFragment() {

}

@Preview
@Composable
private fun VisibleNavigationCompose() {

}

@Preview
@Composable
private fun VisibleKeyboardObstruction() {

}

@Preview
@Composable
private fun VisibleBottomSheet() {

}

private class VisibilityAwareModifierNode(var visibleEventCallback: (VisibleEvent) -> Unit) :
    GlobalPositionAwareModifierNode, Modifier.Node() {

    private var isVisible = false
    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        coroutineScope.launch {
            val visible = coordinates.isVisible()
            if (isVisible != visible) {
                val bounds = coordinates.boundsInWindow()
                val size = coordinates.size
                val fractionVisibleWidth = bounds.width / size.width.toFloat()
                val fractionVisibleHeight = bounds.height / size.height.toFloat()
                visibleEventCallback(VisibleEvent(visible, bounds, size, fractionVisibleHeight, fractionVisibleWidth))
                isVisible = visible
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        visibleEventCallback(VisibleEvent(false, Rect.Zero, IntSize.Zero, 0f, 0f))
        isVisible = false
    }

}
private data class VisibilityAwareModifierElement(val visibleEventCallback: (VisibleEvent)-> Unit) : ModifierNodeElement<VisibilityAwareModifierNode>() {
    override fun create() = VisibilityAwareModifierNode(visibleEventCallback)

    override fun update(node: VisibilityAwareModifierNode) {
        node.visibleEventCallback = visibleEventCallback
    }
}
fun Modifier.onVisibleChange(visibleEvent: (VisibleEvent) -> Unit) =
    this.then(VisibilityAwareModifierElement(visibleEvent))


data class VisibleEvent(
    val isVisible: Boolean,
    val visibleRect: Rect,
    val size: IntSize,
    val fractionVisibleWidth: Float,
    val fractionVisibleHeight: Float
)
/**
 * Determines if a composable's [LayoutCoordinates] is visible.
 *
 * This method uses [boundsInWindow] which returns the composable's `window` clipped bounded [Rect].
 * This does not work for composables obstructed from view by the soft keyboard. This does work for
 * non-full screen composables like [androidx.compose.ui.window.Dialog].
 */
fun LayoutCoordinates.isVisible(): Boolean =
    isAttached && boundsInWindow().width > 0 && boundsInWindow().height > 0

