import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij") version "1.7.0"
    kotlin("jvm") version "1.6.10"
}
val kotlinVersion = project.getKotlinPluginVersion()

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

repositories {
    mavenCentral()
}

intellij {
    version.set("IC-2022.2")
    pluginName.set("jgiven-intellij-plugin")
    plugins.set(listOf("java"))
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))

    testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:1.2.2")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.10")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito:mockito-core:4.6.1")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
