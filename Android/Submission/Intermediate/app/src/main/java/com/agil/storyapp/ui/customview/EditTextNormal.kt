package com.agil.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.agil.storyapp.R

class EditTextNormal : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        minHeight = 48
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) showClearButton() else hideClearButton()
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                clearButtonImage =
                    ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                return when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        true
                    }
                    MotionEvent.ACTION_DOWN -> {
                        showClearButton()
                        true
                    }
                    else -> false
                }
            }
        } else return false
        return false
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}