package com.gurunars.storybook.example

import android.content.Context
import android.view.View
import com.gurunars.storybook_annotations.StorybookComponent
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView

@StorybookComponent
fun Context.sampleComponent(): View =
    frameLayout {
        textView {
            text = getString(R.string.title)
        }
    }