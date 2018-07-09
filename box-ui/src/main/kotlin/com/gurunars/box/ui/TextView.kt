@file:Suppress("NOTHING_TO_INLINE")

package com.gurunars.box.ui

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox

/**
 * Basic font style for TextView.
 * @property value reference to font style integer
 */
enum class Style(val value: Int) {
    /***/
    NORMAL(Typeface.NORMAL),
    /***/
    BOLD(Typeface.BOLD),
    /***/
    ITALIC(Typeface.ITALIC),
    /***/
    BOLD_ITALIC(Typeface.BOLD_ITALIC)
}

/** Binds to a boxed value color int. */
inline fun TextView.textColor(field: IRoBox<Int>) =
    field.onChange {
        setTextColor(it)
    }

/** Binds to a boxed style. */
inline fun TextView.style(field: IRoBox<Style>) =
    field.onChange {
        setTypeface(typeface, it.value)
    }

/** Creates a one way binding from a boxed string value to field's value value. */
inline fun TextView.text(field: IRoBox<String>) =
    field.onChange { txt ->
        if (text.toString() != txt) {
            text = txt
        }
    }

/** Binds to a boxed float based value textSize. */
inline fun TextView.textSize(field: IRoBox<Float>) =
    field.onChange {
        setTextSize(it)
    }

/** Creates a two way binding between a boxed string value to field's value value. */
inline fun EditText.text(field: IBox<String>) {
    field.onChange { txt ->
        if (text.toString() != txt) {
            setText(txt)
        }
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            field.set(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}
