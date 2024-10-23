import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij.platform") version "2.1.0"
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
}
val kotlinVersion = project.getKotlinPluginVersion()

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    buildSearchableOptions {
        enabled = false
    }
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        bundledPlugin("com.intellij.java")
        instrumentationTools()

        testFramework(TestFrameworkType.Plugin.Java)
    }

    implementation(kotlin("stdlib", kotlinVersion))

    testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:1.3.0")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.10")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-core:5.14.1")

    testImplementation("org.opentest4j:opentest4j:1.3.0")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
