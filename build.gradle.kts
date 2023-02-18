import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3

val arrowVersion = "1.2.4"
val jodaMoneyVersion = "2.0.3"
val kotestVersion = "6.0.7"
val kotestArrowVersion = "2.0.0"
val documentationAnnotationsVersion = "0.1.7"

plugins {
    kotlin("jvm") version "2.3.0"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/sylvaindecout/documentation-annotations")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.arrow-kt", "arrow-core", arrowVersion)

    implementation("fr.sdecout.annotations", "documentation-annotations", documentationAnnotationsVersion)

    implementation("org.joda", "joda-money", jodaMoneyVersion)

    testImplementation("io.kotest", "kotest-runner-junit5", kotestVersion)
    testImplementation("io.kotest", "kotest-property", kotestVersion)
    testImplementation("io.kotest", "kotest-assertions-core", kotestVersion)
    testImplementation("io.kotest.extensions", "kotest-assertions-arrow", kotestArrowVersion)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JVM_21)
        languageVersion.set(KOTLIN_2_3)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
