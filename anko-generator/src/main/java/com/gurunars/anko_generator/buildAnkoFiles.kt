package com.gurunars.anko_generator

import android.app.Activity
import android.content.Context
import android.view.ViewManager
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.reflect.KClass

fun ProcessingEnvironment.buildAnkoFiles(
    roundEnv: RoundEnvironment
) {

    val kaptKotlinGeneratedDir = options[AnkoGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME] ?: run {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "Can't find the target directory for generated Kotlin files.")
        return
    }

    roundEnv
        .getElementsAnnotatedWith(AnkoComponent::class.java)
        .filter { it.kind == ElementKind.CLASS }
        .forEach { processElement(kaptKotlinGeneratedDir, it as TypeElement) }
}

fun ProcessingEnvironment.note(msg: String) = messager.printMessage(Diagnostic.Kind.NOTE, msg)
fun ProcessingEnvironment.error(msg: String) = messager.printMessage(Diagnostic.Kind.ERROR, msg)
fun ProcessingEnvironment.warning(msg: String) = messager.printMessage(Diagnostic.Kind.WARNING, msg)

fun ProcessingEnvironment.processElement(
    kaptKotlinGeneratedDir: String,
    element: TypeElement
) {
    note("Processing ${element.simpleName}")
    val componentName = element.simpleName.toString()
    val packageName = elementUtils.getPackageOf(element).qualifiedName.toString()
    val generatedClassName = "Anko$componentName"

    // TODO: make sure that the component is a view subclass
    // TODO: make sure that the first parameter has a Context type

    fun ExecutableElement.allButFirstParams() =
        ParameterSpec.parametersOf(this).drop(1)

    fun getFunSpec(
        reciever: KClass<*>,
        constructor: ExecutableElement
    ) = FunSpec
        .builder(componentName.decapitalize())
        .addTypeVariables(element.typeParameters.map { it.asTypeVariableName() })
        .receiver(reciever)
        .addParameters(constructor.allButFirstParams())
        .addParameter(
            "init",
            LambdaTypeName.get(
                element.asType().asTypeName(),
                returnType = Unit::class.java.asTypeName()))
        .addCode("return ankoView({ $componentName(${
        (listOf("it") + constructor.allButFirstParams().map {
            it.name
        }).joinToString()
        }) }, 0, init)")
        .returns(element.asType().asTypeName())
        .build()

    val builder = FileSpec
        .builder(packageName, generatedClassName)
        .addStaticImport("org.jetbrains.anko.custom", "ankoView")

    element.enclosedElements.filter { it.kind == ElementKind.CONSTRUCTOR }.forEach {
        it as ExecutableElement
        builder
            .addFunction(getFunSpec(ViewManager::class, it))
            .addFunction(getFunSpec(Activity::class, it))
            .addFunction(getFunSpec(Context::class, it))
    }

    val file = builder.build()
    try {
        val string = StringBuilder()
        file.writeTo(string)

        var value = string.toString()
        listOf(
            "java.lang.Enum",
            "java.util.Map",
            "java.util.List"
        ).forEach {
            value = value.replace("import $it", "")
        }
        File(kaptKotlinGeneratedDir, "${generatedClassName.decapitalize()}.kt").apply {
            parentFile.mkdirs()
            writeText(value)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}