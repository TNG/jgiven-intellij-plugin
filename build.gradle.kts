import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij") version "1.3.0"
    kotlin("jvm") version "1.6.10"
}
val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

repositories {
    jcenter()
    mavenCentral()
}

intellij {
    version.set("IC-2021.3")
    pluginName.set("jgiven-intellij-plugin")

    plugins.set(listOf("java"))
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))

    testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:1.2.0")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.8")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-core:4.2.0")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
