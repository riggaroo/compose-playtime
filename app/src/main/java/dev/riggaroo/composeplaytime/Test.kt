package dev.riggaroo.composeplaytime

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Cubic
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.rectangle
import kotlin.math.max

@Preview
@Composable
private fun Preview2() {
    Surface(
        modifier = Modifier,
        shape = RoundedPolygonShape(RoundedPolygon.rectangle(rounding = CornerRounding(0.5f, smoothing = 1f))),
        content = {
            Text("Hello", textAlign = TextAlign.Center,
                modifier = Modifier.size(64.dp))
        }
    )
}

/**
 * Gets a [Path] representation for a [RoundedPolygon] shape, which can be used to draw the
 * polygon.
 *
 * @param path an optional [Path] object which, if supplied, will avoid the function having
 * to create a new [Path] object
 */
@JvmOverloads
fun RoundedPolygon.toPath(path: Path = Path()): Path {
    pathFromCubics(path, cubics)
    return path
}
private fun pathFromCubics(
    path: Path,
    cubics: List<Cubic>
) {
    var first = true
    path.rewind()
    for (element in cubics) {
        if (first) {
            path.moveTo(element.anchor0X, element.anchor0Y)
            first = false
        }
        path.cubicTo(
            element.control0X, element.control0Y, element.control1X, element.control1Y,
            element.anchor1X, element.anchor1Y
        )
    }
    path.close()
}
fun RoundedPolygon.getBounds() = calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }

class RoundedPolygonShape(
    private val polygon: RoundedPolygon,
    private var matrix: Matrix? = null
) : Shape {
    private val path = Path()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        path.rewind()
        polygon.toPath(path)
        if (matrix == null) {
            matrix = Matrix()
            val bounds = polygon.getBounds()
            val maxDimension = max(bounds.width, bounds.height)
            matrix!!.scale(size.width / maxDimension, size.height / maxDimension)
            matrix!!.translate(-bounds.left, -bounds.top)
        }
        path.transform(matrix!!)
        return Outline.Generic(path)
    }
}