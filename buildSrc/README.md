# styled-android-dokka

Gradle plugin to add more reasonable styling to the plain HTML output of Kotlin's Dokka
documentation tool.

Tailored for multi-module Android library packages written in Kotlin already using Dokka Gradle
plugin for API documentation.

## Features

1. Supports special images and other static files if they are located in a **static** folder
   of the respective module.
1. Supports image inclusion in the documentation via a special format of **a** tags:

        [256x456](static/open_close.gif)

   ,where text part is a set of **widthXheight** pixel dimensions and url is the image's src.

1. Generates index page for the whole project with a set of high level descriptions and installation
   instructions obtained from module level build.gradle.
1. Supports cross module references without merging documentation into a single instance

## Install

Assuming that you have managed to generate plain HTML documentation in accordance with
[Dokka's README](https://github.com/Kotlin/dokka) modify your project level build.gradle this way:

    buildscript {
        ...
        ext.mavenRepoUrl = 'https://path/to/your/maven/repo'
        ...
    }

    dependencies {
        ...
        classpath 'com.github.gurunars:styled-android-dokka:VERSION'
        ...
    }

    apply plugin: "styled-android-dokka"

