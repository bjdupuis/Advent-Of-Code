import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

application {
    mainClass.set("util.Runner")
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:multik-api:0.1.1")
    implementation("org.jetbrains.kotlinx:multik-default:0.1.1")
    implementation("org.reflections", "reflections", "0.9.12")
    testImplementation("junit", "junit", "4.13.1")
    testImplementation("org.hamcrest", "hamcrest", "2.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
