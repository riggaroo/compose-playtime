package dev.riggaroo.composeplaytime

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints

fun Modifier.onVisibleChange(visibleEvent: (VisibleEvent) -> Unit) = composed {
    var visible by remember { mutableStateOf(false) }
    var visibleRect by remember { mutableStateOf(Rect.Zero)}

    LaunchedEffect(visible) {
        visibleEvent(VisibleEvent(visible, visibleRect))
    }
    this.onGloballyPositioned {
        visibleRect = it.boundsInWindow()
        visible = it.isVisible()
    }
}

data class VisibleEvent(
    val isVisible: Boolean,
    val visibleRect: Rect
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
fun Modifier.visible(visible: Boolean) = if (visible) this else this.then(Invisible)

private object Invisible : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {}
    }
}
