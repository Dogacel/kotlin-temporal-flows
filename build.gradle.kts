plugins {
    kotlin("multiplatform") version "1.7.21"

    id("com.ncorti.ktfmt.gradle") version "0.11.0"

    id("org.jetbrains.dokka") version "1.7.20"
    id("ru.vyarus.mkdocs") version "3.0.0"
}

group = "dogacel"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
    }
}

// Formatting
ktfmt {
    kotlinLangStyle()
}

tasks.register<com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask>("ktfmtPrecommit") {
    source = project.fileTree(rootDir)
    include("**/*.kt")
}

// Documentation
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
        named("jvmMain") { // The same name as in Kotlin Multiplatform plugin, so the sources are fetched automatically
            val markdownFiles = project.kotlin.sourceSets["jvmMain"].kotlin.files.filter { it.extension == "md" }
            includes.from(markdownFiles)
//            samples.from("samples/basic.kt", "samples/advanced.kt")
        }
    }
}


tasks.dokkaGfm.configure {
    outputDirectory.set(file("${mkdocs.sourcesDir}/docs/api"))
}

tasks.mkdocsBuild.configure {
    dependsOn(tasks.dokkaGfm)
}
