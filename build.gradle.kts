import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val jvmVersion = JvmTarget.JVM_17
val arrowVersion = "1.2.4"
val jodaMoneyVersion = "1.0.5"
val kotestVersion = "5.9.0"
val kotestArrowVersion = "1.4.0"
val documentationAnnotationsVersion = "0.1.7"

plugins {
    kotlin("jvm") version "2.0.20"
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

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }
    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
