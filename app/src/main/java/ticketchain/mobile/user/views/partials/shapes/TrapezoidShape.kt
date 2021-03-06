package ticketchain.mobile.user.views.partials.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object TrapezoidShape : Shape {

    fun generatePath(size: Size) = Path().apply {
        reset()
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, size.height - size.width / 3f)
        lineTo(0f, size.height)
        close()
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(generatePath(size))
}