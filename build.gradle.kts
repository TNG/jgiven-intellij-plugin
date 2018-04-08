import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.PublishTask

val jetbrainsPublishUsername by project
val jetbrainsPublishPassword by project
val jetbrainsPublishChannel by project

plugins {
    id("org.jetbrains.intellij") version "0.3.1"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
    mavenCentral()
}

intellij {
    version = "IC-2018.1"
    pluginName = "jgiven-intellij-plugin"
}

val publishPlugin: PublishTask by tasks

publishPlugin {
    username(jetbrainsPublishUsername)
    password(jetbrainsPublishPassword)
    channels(jetbrainsPublishChannel)
}

dependencies {
    testCompile("com.tngtech.jgiven:jgiven-junit:0.15.3")
    testCompile("com.tngtech.junit.dataprovider:junit4-dataprovider:2.3")
    testCompile("org.assertj:assertj-core:3.9.1")
    testCompile("org.mockito:mockito-core:2.17.0")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)