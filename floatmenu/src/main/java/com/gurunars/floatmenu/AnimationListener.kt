package com.gurunars.floatmenu

/**
 * Handler for FAB open/close animations.
 */
interface AnimationListener {

    /**
     * Called when the animation is initiated.
     *
     * @param projectedDuration time in millis that theoretically should
     */
    fun onStart(projectedDuration: Int)

    /**
     * Called when the animation is completed.
     */
    fun onFinish()

    class Default : AnimationListener {
        override fun onStart(projectedDuration: Int) {

        }

        override fun onFinish() {

        }
    }

}
