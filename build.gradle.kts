import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij.platform") version "2.0.1"
    id("org.jetbrains.intellij.platform.migration") version "2.0.1"
    kotlin("jvm") version "2.0.20"
}
//val kotlinVersion = project.getKotlinPluginVersion()

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
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

//intellij {
//    version.set("IC-2024.2")
//    pluginName.set("jgiven-intellij-plugin")
//    plugins.set(listOf("java"))
//}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.2")
        bundledPlugin("com.intellij.java")
        instrumentationTools()

        testFramework(TestFrameworkType.Plugin.Java)
    }

    //implementation(kotlin("stdlib", kotlinVersion))

    //testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:1.3.0")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.10")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito:mockito-core:5.13.0")
    testImplementation("org.opentest4j:opentest4j:1.3.0")

    //testRuntimeOnly("junit:junit:4.13.2")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
