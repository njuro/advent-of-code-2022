import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "com.github.njuro"
version = "1.0-SNAPSHOT"

val javaVersion = JavaVersion.VERSION_1_8
java.sourceCompatibility = javaVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")
    implementation("org.matheclipse:matheclipse-core:2.0.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    kotlinOptions.jvmTarget = javaVersion.toString()
}
