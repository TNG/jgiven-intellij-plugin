import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
}
val kotlinVersion = project.getKotlinPluginVersion()

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    withType<JavaCompile> {
        options.release.set(21)
    }
    buildSearchableOptions {
        enabled = false
    }
    patchPluginXml {
        sinceBuild.set("262")
        untilBuild.set(provider { null })
    }
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        snapshots()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.2.1") {
            useInstaller = false
        }
        jetbrainsRuntime()
        bundledPlugin("com.intellij.java")

        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.Plugin.Java)
    }

    implementation(kotlin("stdlib", kotlinVersion))

    testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:1.3.0")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.10")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-core:5.18.0")

    testImplementation("org.opentest4j:opentest4j:1.3.0")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
