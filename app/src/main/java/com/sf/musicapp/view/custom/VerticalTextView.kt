package com.sf.musicapp.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView


// custom text view dọc
class VerticalTextView(context:Context,attrs: AttributeSet): AppCompatTextView(context,attrs) {
    private var topDown: Boolean=false

    init{
        val gravity = getGravity()
        if (Gravity.isVertical( gravity ) && ( gravity and Gravity.VERTICAL_GRAVITY_MASK )== Gravity.BOTTOM )
        {
            setGravity(
                ( gravity and Gravity.HORIZONTAL_GRAVITY_MASK ) or Gravity.TOP )
            topDown = false
        }
        else
        {
            topDown = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int,heightMeasureSpec:Int ) {
        super.onMeasure( heightMeasureSpec,
            widthMeasureSpec )
        setMeasuredDimension(
            measuredHeight,
            measuredWidth
        )
    }

    override fun onDraw(canvas: Canvas) {
        val textPaint = paint
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState

        canvas.save()

        if ( topDown ) {
            canvas.translate( width.toFloat(), 0f )
            canvas.rotate( 90f )
        }
        else {
            canvas.translate( 0f, height.toFloat() )
            canvas.rotate( -90f )
        }

        canvas.translate( compoundPaddingLeft.toFloat(),
            extendedPaddingTop.toFloat() )

        layout.draw( canvas )
        canvas.restore()
    }
}