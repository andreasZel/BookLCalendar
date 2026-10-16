package com.andreaszel.booklcalendar.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Palette

object ComicIcons {
    val Book by lazy { icon(2.4f, Line("M4 5.5A2.5 2.5 0 0 1 6.5 3H20v15H6.5A2.5 2.5 0 0 0 4 20.5z"), Line("M4 20.5A2.5 2.5 0 0 0 6.5 23H20v-5")) }
    val Plus by lazy { icon(3f, Line("M12 5v14"), Line("M5 12h14")) }
    val Close by lazy { icon(3f, Line("M6 6l12 12"), Line("M18 6L6 18")) }
    val Back by lazy { icon(3f, Line("M15 5l-7 7 7 7")) }
    val Edit by lazy { icon(2.4f, Line("M4 20h4L19 9l-4-4L4 16z"), Line("M13 7l4 4")) }
    val Trash by lazy { icon(2.4f, Line("M4 7h16"), Line("M9 7V4h6v3"), Line("M6 7l1 13h10l1-13")) }
    val Camera by lazy { icon(2.4f, Line("M3 8h4l2-3h6l2 3h4v12H3z"), Line(circle(12f, 13.5f, 3.5f))) }
    val Gallery by lazy { icon(2.4f, Line("M5 4h14a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z"), Line(circle(9f, 10f, 2f)), Line("M21 16l-5-5-9 9")) }
    val Calendar by lazy { icon(2.4f, Line("M5 5h14a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2z"), Line("M3 10h18M8 3v4M16 3v4")) }
    val Check by lazy { icon(3.2f, Line("M5 12l5 5L20 7")) }
    val Sparkle by lazy { icon(2.2f, Line("M12 2l2.2 6.3L20.5 10.5l-6.3 2.2L12 19l-2.2-6.3L3.5 10.5l6.3-2.2z", Palette.White), Line("M19 16l.9 2.1L22 19l-2.1.9L19 22l-.9-2.1L16 19l2.1-.9z", Palette.White)) }
    val Pinch by lazy { icon(2.2f, Line("M4 4l5 5"), Line("M4 9V4h5"), Line("M20 20l-5-5"), Line("M20 15v5h-5")) }

    val AddCover by lazy {
        ImageVector.Builder("AddCover", 92.dp, 80.dp, 92f, 80f)
            .line("M21 10h36a5 5 0 0 1 5 5v52a5 5 0 0 1-5 5H21a5 5 0 0 1-5-5V15a5 5 0 0 1 5-5z", 3f, Palette.Lilac)
            .line("M26 10v62", 3f)
            .line(circle(44f, 32f, 7f), 3f, Palette.Cream)
            .line(circle(70f, 58f, 15f), 3f, Palette.Yellow)
            .line("M70 51v14M63 58h14", 3f)
            .build()
    }

    private fun icon(stroke: Float, vararg lines: Line): ImageVector {
        val builder = ImageVector.Builder(defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        lines.forEach { builder.line(it.path, stroke, it.fill) }
        return builder.build()
    }

    private fun ImageVector.Builder.line(path: String, stroke: Float, fill: Color? = null): ImageVector.Builder = addPath(pathData = addPathNodes(path), fill = fill?.let { SolidColor(it) }, stroke = SolidColor(Palette.Ink), strokeLineWidth = stroke, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round)

    private fun circle(cx: Float, cy: Float, r: Float) = "M${cx - r} ${cy}a$r $r 0 1 0 ${r * 2} 0a$r $r 0 1 0 ${-r * 2} 0z"

    private class Line(val path: String, val fill: Color? = null)
}
