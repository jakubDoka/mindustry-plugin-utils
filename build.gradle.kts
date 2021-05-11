plugins {
    kotlin("jvm") version "1.4.31"
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.jitpack.io") }
}

var junitVersion = "5.6.1"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.github.Anuken.Arc:arc-core:v126.2")
    compileOnly("com.github.Anuken.Mindustry:core:v126.2")
    implementation("com.beust:klaxon:5.5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "14"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "14"
    }
}
