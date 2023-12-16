import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDate

plugins {
    application
    kotlin("jvm") version "1.7.21"
}

application {
    mainClass.set(project.findProperty("main").toString())
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:multik-api:0.1.1")
    implementation("org.jetbrains.kotlinx:multik-default:0.1.1")
    implementation("org.reflections", "reflections", "0.9.12")
    implementation("joda-time:joda-time:2.10.10")
    testImplementation("junit", "junit", "4.13.1")
    testImplementation("org.hamcrest", "hamcrest", "2.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}



tasks.test {
    filter {
        val year = LocalDate.now().year
        val day = LocalDate.now().dayOfMonth

        // include today's tests
        includeTestsMatching("days.aoc${year}.Day${day}Test.*")

        includeTestsMatching("*PathfindingTest*")
    }
}

