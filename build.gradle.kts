import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String by project
val jetbrainsPublishPassword: String by project
val jetbrainsPublishChannel: String by project

plugins {
    id("org.jetbrains.intellij") version "0.4.4"
    kotlin("jvm") version "1.3.21"
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
    version = "IC-2018.3"
    pluginName = "jgiven-intellij-plugin"
}

val publishPlugin: PublishTask by tasks

publishPlugin {
    username(jetbrainsPublishUsername)
    password(jetbrainsPublishPassword)
    channels(jetbrainsPublishChannel)
}

dependencies {
    compile(kotlin("stdlib", kotlinVersion))

    testCompile("com.tngtech.jgiven:jgiven-junit:0.15.3")
    testCompile("com.tngtech.junit.dataprovider:junit4-dataprovider:2.3")
    testCompile("org.assertj:assertj-core:3.9.1")
    testCompile("org.mockito:mockito-core:2.24.5")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)