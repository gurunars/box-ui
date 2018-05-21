package com.gurunars.box.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import com.gurunars.box.ui.ConsumerField


private class BorderDrawable(
    private val borderSize: Int = 1,
    private val borderRadius: Int = 0,
    @ColorInt private val borderColor: Int = Color.BLACK
) : Drawable() {

    private val paint = Paint().apply {
        color = borderColor
    }

    override fun draw(canvas: Canvas) {
        val height = bounds.height().toFloat()
        val width = bounds.width().toFloat()

        val r = borderRadius.toFloat()

        canvas.drawRoundRect(
            RectF(0f, 0f, width, height),
            r, r,
            paint
        )

        val sr = r - borderSize

        canvas.drawRoundRect(
            RectF(
                0f + borderSize,
                0f + borderSize,
                width - borderSize,
                height - borderSize
            ),
            sr, sr,
            Paint().apply {
                color = Color.TRANSPARENT
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }
        )
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

}


private class CharDrawable(
    private val size: Float,
    private val textColor: Int,
    char: Char
) : Drawable() {

    private val text = char.toString()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
        textSize = size
    }

    private val textBounds = Rect().apply {
        paint.getTextBounds(text, 0, text.length, this)
    }

    override fun draw(canvas: Canvas) =
        canvas.drawText(
            text,
            0,
            text.length,
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat() + textBounds.height() / 2,
            paint
        )

    override fun getOpacity(): Int =
        paint.alpha

    override fun getIntrinsicWidth(): Int =
        textBounds.width()

    override fun getIntrinsicHeight(): Int =
        textBounds.height()

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(filter: ColorFilter?) {
        paint.colorFilter = filter
    }

}

private class PlaceholderDrawable(
    private val size: Float,
    private val textColor: Int
) : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            bounds.width().toFloat() / 2,
            bounds.height().toFloat() / 2,
            size / 3,
            paint
        )
    }

    override fun setAlpha(alpha: Int) { paint.alpha = alpha }

    override fun getOpacity(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) { paint.colorFilter = colorFilter }
}


private fun getBorderedChar(
    charsVisible: Boolean,
    textColor: Int,
    hintColor: Int,
    lineSize: Int,
    fontSize: Float,
    char: Char?
) = LayerDrawable(listOfNotNull(
    BorderDrawable(
        borderSize = lineSize,
        borderColor = textColor.takeIf { char != null } ?: hintColor
    ),
    char?.let {
        if (charsVisible) {
            CharDrawable(
                fontSize,
                textColor,
                char
            )
        } else {
            PlaceholderDrawable(fontSize, textColor)
        }
    }
).toTypedArray())


private fun Drawable.toBitmap(width: Int = -1, height: Int = -1): Bitmap {
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    val bitmapWidth = if (width < 0) intrinsicWidth else width
    val bitmapHeight = if (height < 0) intrinsicHeight else height
    val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

private fun exactMeasure(value: Int) =
    View.MeasureSpec.makeMeasureSpec(value, View.MeasureSpec.EXACTLY)

interface EditPinCodeProps {
    var charsVisible: Boolean
    var pinLength: Int
    var fontSize: Float
}

class EditPinCode(context: Context) : EditText(context), EditPinCodeProps {

    private val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        isDither = true
    }

    private var pinWidth: Int = 0
    private var pinHeight: Int = 0
    private var pinSpace: Int = 0

    private var onClickListener: OnClickListener? = null
    private var onEditorActionListener: OnEditorActionListener? = null

    override var charsVisible: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    override var fontSize: Float = 15f
        set(value) {
            field = value
            textSize = value
            invalidate()
        }

    override var pinLength: Int = 4
        set(value) {
            field = value
            invalidate()
        }

    private fun configure() {
        pinWidth = (textSize * 1.7f).toInt()
        pinHeight = (textSize * 2).toInt()
        pinSpace = textSize.toInt()
        filters = arrayOf(InputFilter.LengthFilter(pinLength))
    }

    override fun invalidate() {
        configure()
        super.invalidate()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            setSelection(text?.length ?: 0)
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun setOnEditorActionListener(onEditorActionListener: OnEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener
    }

    override fun isSuggestionsEnabled(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val totalWidthPinSize = pinWidth * pinLength
        val totalSpaceHorizontalSize = pinSpace * (pinLength - 1)

        val newWidth = paddingStart + paddingEnd + totalWidthPinSize + totalSpaceHorizontalSize
        val newHeight = paddingTop + pinHeight + paddingBottom

        setMeasuredDimension(
            exactMeasure(newWidth),
            exactMeasure(newHeight)
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val paddingTop: Float = paddingTop.toFloat()
        val paddingStart: Float = paddingStart.toFloat()
        val canvasClipBondsTop: Int = canvas.clipBounds.top
        val top: Float = paddingTop + canvasClipBondsTop
        val lineSize = textSize / 10

        for (i in 0 until pinLength) {
            // Canvas's top sometimes will have extra top pinSpace
            val left: Float = paddingStart + (pinSpace * i) + (pinWidth * i)

            val pinDrawable = getBorderedChar(
                charsVisible = charsVisible,
                textColor = textColors.defaultColor,
                hintColor = hintTextColors.defaultColor,
                lineSize = lineSize.toInt(),
                fontSize = textSize,
                char = text?.getOrNull(i)
            )
            canvas.drawBitmap(
                pinDrawable.toBitmap(pinWidth, pinHeight),
                left,
                top,
                paint
            )
        }

    }

    private fun initClickListener() =
        super.setOnClickListener { view ->
            setSelection(text?.length ?: 0)
            onClickListener?.onClick(view)
        }

    private fun initOnEditorActionListener() =
        super.setOnEditorActionListener { view, actionId, event ->
            onEditorActionListener?.onEditorAction(view, actionId, event) ?: false
        }

    private class ActionModeCallbackInterceptor : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = false
        override fun onDestroyActionMode(mode: ActionMode) {}
    }

    init {
        isCursorVisible = false
        isLongClickable = false
        customSelectionActionModeCallback = ActionModeCallbackInterceptor()
        maxLines = 1
        setBackgroundColor(Color.TRANSPARENT)
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        initClickListener()
        initOnEditorActionListener()

        setOnKeyListener { _, _, _ ->
            setSelection(text?.length ?: 0)
            false
        }

        addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                setSelection(text?.length ?: 0)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        configure()
    }

}

fun Context.editPinCode(init: EditPinCodeProps.() -> Unit): View = EditPinCode(this).apply { init() }

var EditPinCodeProps.charsVisible_ by ConsumerField<EditPinCodeProps, Boolean> { charsVisible = it }
var EditPinCodeProps.pinLength_ by ConsumerField<EditPinCodeProps, Int> { pinLength = it }
var EditPinCodeProps.fontSize_ by ConsumerField<EditPinCodeProps, Float> { fontSize = it }