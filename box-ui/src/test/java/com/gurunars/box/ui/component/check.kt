package com.gurunars.box.ui.component

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever

/**
 * TODO:
 *
 * I need to clearly report the missing mutation
 * so that the test writer could easily say what has
 * to be fixed.
 *
 * But I don't need to report the error in the
 * sequence of mutations.
 *
 * Mutation sequences should be tested separately.
 *
 * I just need to verify the actual calls perform
 * on the entity and make sure that there is only one
 * of each.
 */

inline fun<reified T: View> List<Mutation>.assertMutations(
    verifier: T.() -> Unit
) {
    val view: T = mock()

    val ctx: Context = mock()

    whenever(view.context).thenReturn(ctx)

    this.forEach { it(view) }
    verify(view, times(1)).verifier()
}
