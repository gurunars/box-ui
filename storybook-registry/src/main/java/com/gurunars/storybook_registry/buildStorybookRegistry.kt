package com.gurunars.storybook_registry

import java.io.File
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

fun ProcessingEnvironment.note(msg: String) = messager.printMessage(Diagnostic.Kind.NOTE, msg)
fun ProcessingEnvironment.error(msg: String) = messager.printMessage(Diagnostic.Kind.ERROR, msg)
fun ProcessingEnvironment.warning(msg: String) = messager.printMessage(Diagnostic.Kind.WARNING, msg)

fun ProcessingEnvironment.buildStorybookRegistry(
    roundEnv: RoundEnvironment
) {

    val kaptKotlinGeneratedDir = options[StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "Can't find the target directory for generated Kotlin files.")
        return
    }

    val elements = roundEnv
        .getElementsAnnotatedWith(StorybookComponent::class.java)
        .map { it as ExecutableElement }

    val pairs = elements.map {
        Pair(
            elementUtils.getPackageOf(it).qualifiedName.toString(),
            it.simpleName.toString()
        )
    }

    val views = pairs.map { """ "${it.first}.${it.second}" to Context:${it.second} """ }
    val imports = pairs.map { "import ${it.first}.${it.second}" }

    val tpl = """
    package com.gurunars.storybook

    // ${elements.size}

    ${imports.joinToString("\n")}

    class ActivityStorybook(): AbstractActivityStorybook() {
        override val views: Map<String, RenderDemo> = mapOf(
            ${views.joinToString(",\n")}
        )
    }
    """

    try {
        File(kaptKotlinGeneratedDir, "ActivityStorybook.kt").apply {
            parentFile.mkdirs()
            writeText(tpl)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}
