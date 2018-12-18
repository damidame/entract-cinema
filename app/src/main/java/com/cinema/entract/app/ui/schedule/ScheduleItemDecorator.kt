/*
 * Copyright 2018 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.app.ui.schedule

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.dip


class ScheduleItemDecorator(val context: Context) : RecyclerView.ItemDecoration() {

    private val keyline = context.resources.getDimension(R.dimen.keyline_list)
    private val marginMedium =
        context.resources.getDimension(R.dimen.margin_padding_size_medium).toInt()
    private val height = context.dip(1)
    private val paint = Paint().apply {
        color = context.colorAttr(R.attr.colorOnBackground)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (0 == position) {
            outRect.setEmpty()
        } else {
            when (parent.adapter?.getItemViewType(position)) {
                ScheduleAdapter.TYPE_WEEK_HEADER -> outRect.top = marginMedium
                ScheduleAdapter.TYPE_DAY_HEADER -> outRect.setEmpty()
                ScheduleAdapter.TYPE_MOVIE -> outRect.bottom = height
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.childCount
        for (i in 0 until itemCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            if (i < itemCount - 1 &&
                ScheduleAdapter.TYPE_MOVIE == parent.adapter?.getItemViewType(position)
            ) {
                when (parent.adapter?.getItemViewType(position + 1)) {
                    ScheduleAdapter.TYPE_MOVIE -> c.drawRect(
                        view.left + view.paddingLeft + keyline,
                        view.bottom.toFloat(),
                        view.right.toFloat(),
                        (view.bottom + height).toFloat(),
                        paint
                    )

                    ScheduleAdapter.TYPE_WEEK_HEADER -> c.drawRect(
                        view.left.toFloat(),
                        view.bottom.toFloat(),
                        view.right.toFloat(),
                        (view.bottom + height).toFloat(),
                        paint
                    )
                }
            }
        }
    }
}