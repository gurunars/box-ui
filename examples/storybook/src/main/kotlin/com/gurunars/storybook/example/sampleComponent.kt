package com.gurunars.storybook.example

import android.content.Context
import com.gurunars.storybook_registry.StorybookComponent
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView

@StorybookComponent
fun Context.sampleComponent() =
    frameLayout {
        textView {
            text="Title"
        }
    }