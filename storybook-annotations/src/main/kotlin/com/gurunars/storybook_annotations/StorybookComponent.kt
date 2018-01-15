package com.gurunars.storybook_annotations

/**
 * Annotation that demarcates a storybook view function.
 *
 * The function should implement the following interface:
 *
 *      Context.() -> View
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class StorybookComponent
