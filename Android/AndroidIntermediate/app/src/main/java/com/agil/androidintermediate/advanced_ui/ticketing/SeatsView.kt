package com.agil.androidintermediate.advanced_ui.ticketing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.agil.androidintermediate.R

class SeatsView: View {
    var seat: Seat? = null
    private val seats: ArrayList<Seat> = arrayListOf(
        Seat(id = 1, name = "A1", isBooked = false),
        Seat(id = 2, name = "A2", isBooked = false),
        Seat(id = 3, name = "B1", isBooked = false),
        Seat(id = 4, name = "A4", isBooked = false),
        Seat(id = 5, name = "C1", isBooked = false),
        Seat(id = 6, name = "C2", isBooked = false),
        Seat(id = 7, name = "D1", isBooked = false),
        Seat(id = 8, name = "D2", isBooked = false),
    )
    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight,heightMeasureSpec)

        val halfOfHeight = height/2
        val halfOfWidth = width/2
        var value = -550F

        for (i in 0..7){
            if (i.mod(2) == 0){
                seats[i].apply {
                    x = halfOfWidth - 300F
                    y = halfOfHeight + value
                }
            }else{
                seats[i].apply {
                    x = halfOfWidth + 100F
                    y = halfOfHeight + value
                }
                value += 300F
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats){
            drawSeat(canvas, seat)
        }

        val text = "Silahkan Pilih Kursi"
        titlePaint.apply {
            textSize = 50F
        }
        canvas?.drawText(text, (width/2)-197F, 100F, titlePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val halfOfWidth = width/2
        val halfOfHeight = height/2

        val widthColumnOne = (halfOfWidth - 300F)..(halfOfWidth - 100F)
        val widthColumnTwo = (halfOfWidth + 100F)..(halfOfWidth + 300F)

        val heightRowOne = (halfOfHeight - 600F)..(halfOfHeight - 400F)
        val heightRowTwo = (halfOfHeight - 300F)..(halfOfHeight - 100F)
        val heightRowThree = (halfOfHeight + 0F)..(halfOfHeight + 200F)
        val heightRowFour = (halfOfHeight + 300F)..(halfOfHeight + 500F)

        if (event!!.action == MotionEvent.ACTION_DOWN){
            when{
                event.x in widthColumnOne && event.y in heightRowOne -> booking(0)
                event.x in widthColumnTwo && event.y in heightRowOne -> booking(1)
                event.x in widthColumnOne && event.y in heightRowTwo -> booking(2)
                event.x in widthColumnTwo && event.y in heightRowTwo -> booking(3)
                event.x in widthColumnOne && event.y in heightRowThree -> booking(4)
                event.x in widthColumnTwo && event.y in heightRowThree -> booking(5)
                event.x in widthColumnOne && event.y in heightRowFour -> booking(6)
                event.x in widthColumnTwo && event.y in heightRowFour -> booking(7)
            }
        }

        return true
    }

    private fun booking(position: Int) {
        for (seat in seats){
            seat.isBooked = false
        }
        seats[position].apply {
            seat = this
            isBooked = true
        }
        invalidate()
    }

    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        if (seat.isBooked){
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.green_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.green_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.green_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        }else{
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }
        //menyimpan state
        canvas?.save()

        //pindah titik sumbu
        canvas?.translate(seat.x as Float, seat.y as Float)

        //background
        val backgroundPath = Path()
        backgroundPath.addRect(0F,0F,200F,200F, Path.Direction.CCW)
        backgroundPath.addCircle(100F,50F,75F, Path.Direction.CCW)
        canvas?.drawPath(backgroundPath, backgroundPaint)

        //sandaran tangan
        val armrestPath = Path()
        canvas?.translate(0F,130F)
        armrestPath.addRect(0F,0F,50F,70F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)
        canvas?.translate(150F,0F)
        armrestPath.addRect(0F,0F,50F,70F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath,armrestPaint)

        //bawah kursi
        canvas?.translate(-150F,40F)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0F,0F,200F,30F, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatPaint)

        //Menulis Nomor Kursi
        canvas?.translate(0F, -170F)
        numberSeatPaint.apply {
            textSize = 50F
            getTextBounds(seat.name, 0, seat.name.length, mBounds)
        }
        canvas?.drawText(seat.name, 100F - mBounds.centerX(), 100F, numberSeatPaint)

        //Mengembalikan kepengaturan sebelumnya
        canvas?.restore()
    }
}