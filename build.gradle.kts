val jvmVersion = "17"
val archunitVersion = "1.0.1"
val arrowVersion = "1.2.1"
val jodaMoneyVersion = "1.0.4"
val junitVersion = "5.3.1"
val mockitoKotlinVersion = "4.1.0"
val striktVersion = "0.34.1"
val documentationAnnotationsVersion = "0.1.7"

plugins {
    kotlin("jvm") version "1.9.20"
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

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junitVersion)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)

    testImplementation(platform("io.strikt:strikt-bom:$striktVersion"))
    testImplementation("io.strikt", "strikt-arrow")

    testImplementation("org.mockito.kotlin", "mockito-kotlin", mockitoKotlinVersion)
    testImplementation("com.tngtech.archunit", "archunit-junit5-api", archunitVersion)
    testImplementation("com.tngtech.archunit", "archunit-junit5-engine", archunitVersion)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = jvmVersion
        }
    }
    compileTestKotlin {
        kotlinOptions{
            jvmTarget = jvmVersion
        }
    }
    test {
        useJUnitPlatform()
    }
}

