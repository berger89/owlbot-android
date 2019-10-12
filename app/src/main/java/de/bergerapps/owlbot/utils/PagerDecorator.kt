package de.bergerapps.owlbot.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagerDecorator : RecyclerView.ItemDecoration() {

    private var paintStroke: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.GREEN
    }

    private val paintFill = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    // save the center coordinates of all indicators
    private val indicators = mutableListOf<Pair<Float, Float>>()

    private val indicatorRadius = 30f
    private val indicatorPadding = 180f

    private var activeIndicator = 0
    private var isInitialized = false

    // create three indicators for three slides
    override fun onDrawOver(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        if (!isInitialized) {
            setupIndicators(parent)
        }

        // draw three indicators with stroke style
        parent.adapter?.let {
            with(canvas) {
                drawCircle(indicators[0].first, indicators[0].second)
                drawCircle(indicators[1].first, indicators[1].second)
                drawCircle(indicators[2].first, indicators[2].second)
            }

            val visibleItem = (parent.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()

            if (visibleItem >= 0) {
                activeIndicator = visibleItem
            }

            // paint over the needed circle
            when (activeIndicator) {
                0 -> canvas.drawCircle(indicators[0].first, indicators[0].second, true)
                1 -> canvas.drawCircle(indicators[1].first, indicators[1].second, true)
                2 -> canvas.drawCircle(indicators[2].first, indicators[2].second, true)
            }
        }
    }

    private fun Canvas.drawCircle(x: Float, y: Float, isFill: Boolean = false) {
        drawCircle(x, y, indicatorRadius, if (isFill) paintFill else paintStroke)
    }

    private fun setupIndicators(recyclerView: RecyclerView) {
        isInitialized = true

        val indicatorTotalWidth = indicatorRadius * 6 + indicatorPadding
        val indicatorPosX = (recyclerView.width - indicatorTotalWidth) / 2f
        val indicatorPosY = recyclerView.height - (indicatorRadius * 6 / 2f)

        indicators.add(indicatorPosX to indicatorPosY)
        indicators.add((indicatorPosX + indicatorRadius) to indicatorPosY)
        indicators.add((indicatorPosX + indicatorRadius * 2) to indicatorPosY)
    }
}