package com.gurunars.anko_generator;

import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
@SupportedOptions(AnkoGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
public class AnkoGenerator extends AbstractProcessor {

    public final static String KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> rval = new HashSet<>();
        rval.add(AnkoComponent.class.getCanonicalName());
        return rval;
    }

    @Override
    public boolean process(
            Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv
    ) {
        BuildAnkoFilesKt.buildAnkoFiles(processingEnv, roundEnv);
        return false;
    }
}