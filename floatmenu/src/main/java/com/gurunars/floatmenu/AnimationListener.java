package com.gurunars.floatmenu;

/**
 * Handler for FAB open/close animations.
 */
public interface AnimationListener {

    /**
     * Called when the animation is initiated.
     *
     * @param projectedDuration time in millis that theoretically should
     */
    void onStart(int projectedDuration);

    /**
     * Called when the animation is completed.
     */
    void onFinish();

    class Default implements AnimationListener {
        @Override
        public void onStart(int projectedDuration) {

        }

        @Override
        public void onFinish() {

        }
    }

}
