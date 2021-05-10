plugins {
    java
    kotlin("jvm") version "1.4.10"
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.github.Anuken.Arc:arc-core:v126.2")
    compileOnly("com.github.Anuken.Mindustry:core:v126.2")
    implementation("com.beust:klaxon:5.5")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}
