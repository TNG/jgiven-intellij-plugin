import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jetbrainsPublishUsername: String? by project
val jetbrainsPublishToken: String? by project

plugins {
    id("org.jetbrains.intellij") version "0.6.5"
    kotlin("jvm") version "1.4.30"
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
    version = "IC-2020.3"
    pluginName = "jgiven-intellij-plugin"

    setPlugins("java")
}

val publishPlugin: PublishTask by tasks

if (jetbrainsPublishToken != null && jetbrainsPublishUsername != null) {
    publishPlugin {
        username(jetbrainsPublishUsername)
        token(jetbrainsPublishToken)
    }
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))

    testImplementation(kotlin("stdlib-jdk7", kotlinVersion))
    testImplementation("com.tngtech.jgiven:jgiven-junit:0.18.2")
    testImplementation("com.tngtech.junit.dataprovider:junit4-dataprovider:2.8")
    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.mockito:mockito-core:3.2.4")
}

inline operator fun <T : Task> T.invoke(a: T.() -> Unit): T = apply(a)
