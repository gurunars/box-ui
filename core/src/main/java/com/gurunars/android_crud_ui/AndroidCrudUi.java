package com.gurunars.android_crud_ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AndroidCrudUi extends LinearLayout {

    private TextView text;

    public AndroidCrudUi(Context context) {
        this(context, null);
    }

    public AndroidCrudUi(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndroidCrudUi(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.android_crud_ui, this);

        text = (TextView) findViewById(R.id.text);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AndroidCrudUi,
                0, 0);

        try {
            String text = a.getString(R.styleable.AndroidCrudUi_text);
            if (text == null) {
                text = context.getString(R.string.default_text);
            }
            this.text.setText("TEST TEXT:" + text);
        } finally {
            a.recycle();
        }

    }

}
