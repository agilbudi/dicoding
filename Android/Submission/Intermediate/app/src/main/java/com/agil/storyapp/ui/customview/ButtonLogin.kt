package com.agil.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.agil.storyapp.R

class ButtonLogin : AppCompatButton {
    private var txtColor: Int = 0
    private lateinit var bgButton: Drawable
    private lateinit var bgButtonDisabled: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        init()
    }

    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val txtLogin = resources.getString(R.string.login)
        val txtLoginDisable = resources.getString(R.string.not_available)
        background = if (isEnabled) bgButton else bgButtonDisabled
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if (isEnabled) txtLogin else txtLoginDisable
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        bgButton = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        bgButtonDisabled =
            ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}