val jvm_version: String by project
val archunit_version: String by project
val arrow_version: String by project
val joda_money_version: String by project
val junit_version: String by project
val mockito_kotlin_version: String by project
val strikt_version: String by project
val documentation_annotations_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
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

    implementation("io.arrow-kt", "arrow-core", arrow_version)

    implementation("fr.sdecout.annotations", "documentation-annotations", documentation_annotations_version)

    implementation("org.joda", "joda-money", joda_money_version)

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junit_version)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", junit_version)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junit_version)

    testImplementation(platform("io.strikt:strikt-bom:$strikt_version"))
    testImplementation("io.strikt", "strikt-arrow")

    testImplementation("org.mockito.kotlin", "mockito-kotlin", mockito_kotlin_version)
    testImplementation("com.tngtech.archunit", "archunit-junit5-api", archunit_version)
    testImplementation("com.tngtech.archunit", "archunit-junit5-engine", archunit_version)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = jvm_version
        }
    }
    compileTestKotlin {
        kotlinOptions{
            jvmTarget = jvm_version
        }
    }
    test {
        useJUnitPlatform()
    }
}

