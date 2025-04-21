import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "org.enoch02"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
}