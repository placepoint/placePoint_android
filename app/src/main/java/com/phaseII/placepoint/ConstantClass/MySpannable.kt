package com.phaseII.placepoint.ConstantClass

import android.graphics.Color
import android.graphics.Color.parseColor
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View


open class MySpannable
/**
 * Constructor
 */
(isUnderline: Boolean) : ClickableSpan() {

    private var isUnderline = false

    init {
        this.isUnderline = isUnderline
    }

    override fun updateDrawState(ds: TextPaint) {

        ds.isUnderlineText = isUnderline
        ds.color = Color.parseColor("#2F3F59")

    }

    override fun onClick(widget: View) {

    }
}
