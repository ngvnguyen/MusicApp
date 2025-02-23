package com.sf.musicapp.view.custom

import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import okhttp3.logging.HttpLoggingInterceptor

object ItemDecoration {
    private var itemDecoration: RecyclerView.ItemDecoration? = null

    fun get(resources: Resources) = itemDecoration?: object: RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position%2

            val density = resources.displayMetrics.density
            val widthItem = 120* density
            val padding = 24 * density

            if (column == 0) {
                val width = parent.width/2
                outRect.left = (width - widthItem - padding).toInt()
            }else outRect.left = padding.toInt()

        }
    }.also { itemDecoration = it }
}
