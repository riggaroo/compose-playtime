package dev.riggaroo.composeplaytime.visibility


import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch

/**
 * `onVisibleChange` fires events when the visibility of an item changes.
 *
 * The event includes information such as the bounds of the visible item, and the fraction of the
 * item that's visible. It'll only fire once for when the item enters the screen, and on exit, using
 * the bounds within the current window to determine if an item is visible or not.
 *
 * _Limitations_:
 *    - This modifier has no way to detect if there is something on top of it - obscuring (ie z-index checking).
 *    - Keyboard visibility, if an item is below the keyboard, this modifier will still track
 *    the item as visible, it is recommended that you ensure you are using
 *    android:windowSoftInputMode="adjustResize" to ensure the whole window is resized and then
 *    items will not report themselves as being visible.
 *
 * See VisibilityTrackingSample.kt for example usage.
 *
 * @param visibleEvent function that will run when the item becomes visible or invisible.
 */
fun Modifier.onVisibleChange(visibleEvent: (VisibleEvent) -> Unit) =
    this.then(VisibilityAwareModifierElement(visibleEvent))

sealed class VisibleEvent {
    data class Visible(
        val visibleRect: Rect,
        val size: IntSize,
        val fractionVisibleWidth: Float,
        val fractionVisibleHeight: Float
    ) : VisibleEvent()

    object Invisible : VisibleEvent()
}

private class VisibilityAwareModifierNode(var visibleEventCallback: (VisibleEvent) -> Unit) :
    GlobalPositionAwareModifierNode, Modifier.Node() {

    private var isVisible = false
    private var visibleBounds = Rect.Zero
    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        coroutineScope.launch {
            val visible = coordinates.isVisible()

            val bounds = coordinates.boundsInWindow()

            val size = coordinates.size
            val fractionVisibleWidth = bounds.width / size.width.toFloat()
            val fractionVisibleHeight = bounds.height / size.height.toFloat()

            // TODO: Decide if we want to check the bounds change too,
            //  it'll fire the event multiple times if we check the bounds... maybe we should have two callbacks,
            //  one for onEnter, onExit and onPositionChanged?
            if (isVisible != visible /*|| visibleBounds != bounds*/) {
                if (visible) {
                    visibleEventCallback(
                        VisibleEvent.Visible(
                            bounds,
                            size,
                            fractionVisibleWidth,
                            fractionVisibleHeight
                        )
                    )
                } else {
                    visibleEventCallback(VisibleEvent.Invisible)
                }

                isVisible = visible
                visibleBounds = bounds
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        visibleEventCallback(VisibleEvent.Invisible)
        isVisible = false
    }

}

private data class VisibilityAwareModifierElement(val visibleEventCallback: (VisibleEvent) -> Unit) :
    ModifierNodeElement<VisibilityAwareModifierNode>() {
    override fun create() = VisibilityAwareModifierNode(visibleEventCallback)

    override fun update(node: VisibilityAwareModifierNode) {
        node.visibleEventCallback = visibleEventCallback
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "visibility aware"
        properties["visibleEventCallback"] = visibleEventCallback
    }
}


/**
 * Determines if a composable's [LayoutCoordinates] is visible.
 *
 * This method uses [boundsInWindow] which returns the composable's `window` clipped bounded [Rect].
 * This does not work for composables obstructed from view by the soft keyboard. This does work for
 * non-full screen composables like [androidx.compose.ui.window.Dialog].
 */
fun LayoutCoordinates.isVisible(): Boolean =
    isAttached && boundsInWindow().width > 0 && boundsInWindow().height > 0
