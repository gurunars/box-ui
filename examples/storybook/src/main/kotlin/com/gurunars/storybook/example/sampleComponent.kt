package com.gurunars.storybook.example

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.gurunars.box.ui.with
import com.gurunars.storybook_annotations.StorybookComponent

@StorybookComponent
fun Context.sampleComponent(): View =
    with<FrameLayout> {
        with<TextView> {
            id=R.id.sampleText
            text = getString(R.string.title)
        }
    }