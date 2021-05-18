package com.gurunars.storybook.example

import android.content.Context
import android.view.View
import com.gurunars.storybook_annotations.StorybookComponent

@StorybookComponent
fun Context.sampleComponent(): View =
    frameLayout {
        textView {
            id=R.id.sampleText
            text = getString(R.string.title)
        }
    }