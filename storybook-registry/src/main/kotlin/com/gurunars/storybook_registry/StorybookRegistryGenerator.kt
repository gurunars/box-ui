package com.gurunars.storybook_registry

import com.gurunars.storybook_annotations.StorybookComponent
import java.io.File
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

private fun ProcessingEnvironment.note(msg: String) = messager.printMessage(Diagnostic.Kind.NOTE, msg)
private fun ProcessingEnvironment.error(msg: String) = messager.printMessage(Diagnostic.Kind.ERROR, msg)
private fun ProcessingEnvironment.warning(msg: String) = messager.printMessage(Diagnostic.Kind.WARNING, msg)

/**
 * Generates a **com.gurunars.storybook.ActivityStorybook** class that implements
 * **com.gurunars.storybook.AbstractActivityStorybook** by injecting all the stories marked
 * by a **@StorybookComponent** annotation.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.gurunars.storybook_annotations.StorybookComponent")
@SupportedOptions(StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class StorybookRegistryGenerator : AbstractProcessor() {

    /** @suppress */
    override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(StorybookComponent::class.java)
        if (elements.isEmpty()) return false

        val kaptKotlinGeneratedDir = processingEnv.options[
            StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
            processingEnv.error("Can't find the target directory for generated Kotlin files.")
            return false
        }

        val pairs = elements.map {
            Pair(
                processingEnv.elementUtils.getPackageOf(it).qualifiedName.toString(),
                it.simpleName.toString()
            )
        }

        val views = pairs.map { " \"${it.first}.${it.second}\" to Context::${it.second} " }
        val imports = pairs.map { "import ${it.first}.${it.second}" }

        val tpl = """
        package com.gurunars.storybook

        import android.content.Context
        import android.view.View

        ${imports.joinToString("\n")}

        class ActivityStorybook(): AbstractActivityStorybook() {
            override val views: Map<String, Context.() -> View> = mapOf(
                ${views.joinToString(",\n" )}
            )
        }
        """

        try {
            File(File(kaptKotlinGeneratedDir).resolveSibling(File("main")),
                    "ActivityStorybook.kt").apply {
                parentFile.mkdirs()
                writeText(tpl.trimIndent())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    /** @suppress */
    companion object {
        /** @suppress */
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}