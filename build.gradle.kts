/*
 * Copyright 2019 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.github.ben-manes.versions") version "0.21.0"
}

buildscript {
    repositories {
        jcenter()
        google()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(kotlin("gradle-plugin", Versions.kotlin))
        classpath(Build.androidGradle)
        classpath(Build.googleServices)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven("https://jitpack.io")
    }
}
