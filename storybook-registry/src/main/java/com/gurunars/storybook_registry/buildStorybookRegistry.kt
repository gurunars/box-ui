package com.gurunars.storybook_registry

import java.io.File
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

fun ProcessingEnvironment.buildAnkoFiles(
    roundEnv: RoundEnvironment
) {

    val kaptKotlinGeneratedDir = options[StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "Can't find the target directory for generated Kotlin files.")
        return
    }

    val mapping = roundEnv
        .getElementsAnnotatedWith(StorybookComponent::class.java)
        .filter { it.kind == ElementKind.METHOD }
        .map { it as ExecutableElement }

    processElement(kaptKotlinGeneratedDir, mapping)
}

fun ProcessingEnvironment.note(msg: String) = messager.printMessage(Diagnostic.Kind.NOTE, msg)
fun ProcessingEnvironment.error(msg: String) = messager.printMessage(Diagnostic.Kind.ERROR, msg)
fun ProcessingEnvironment.warning(msg: String) = messager.printMessage(Diagnostic.Kind.WARNING, msg)

fun ProcessingEnvironment.processElement(
    kaptKotlinGeneratedDir: String,
    elements: List<ExecutableElement>
) {

    val views = elements.map {
        val componentName = it.simpleName.toString()
        val packageName = elementUtils.getPackageOf(it).qualifiedName.toString()
        """ "$packageName.$componentName" to Context:$componentName """
    }

    val imports = elements.map {
        val componentName = it.simpleName.toString()
        val packageName = elementUtils.getPackageOf(it).qualifiedName.toString()
        "import ${packageName}.$componentName"
    }

    val tpl = """
    package com.gurunars.storybook

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