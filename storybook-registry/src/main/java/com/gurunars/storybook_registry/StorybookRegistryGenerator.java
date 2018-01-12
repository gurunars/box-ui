package com.gurunars.storybook_registry;

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

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
@SupportedOptions(StorybookRegistryGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
public class StorybookRegistryGenerator extends AbstractProcessor {

    public final static String KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> rval = new HashSet<>();
        rval.add(StorybookComponent.class.getCanonicalName());
        return rval;
    }

    @Override
    public boolean process(
            Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv
    ) {
        BuildStorybookRegistryKt.buildAnkoFiles(processingEnv, roundEnv);
        return false;
    }
}