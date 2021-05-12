plugins {
    java
    kotlin("jvm") version "1.4.10"
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.jitpack.io") }
}

var junitVersion = "5.6.1"
var mindustryVer = "v126.2"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVer")
    compileOnly("com.github.Anuken.Mindustry:core:$mindustryVer")
    implementation("com.beust:klaxon:5.5")
    implementation("com.discord4j:discord4j-core:3.1.5")
    testImplementation("com.discord4j:discord4j-core:3.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    "test"(Test::class) {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        reports.html.isEnabled = true
    }
}

/** Artifact groupId. */
group = "com.developerlife"

/** Artifact version. Note that "SNAPSHOT" in the version is not supported by bintray. */
version = "v0.1.1"

/** This is from settings.gradle.kts, is "color-console". */
val myArtifactId: String = rootProject.name

/** This is defined above as `group`, is "com.developerlife". */
val myArtifactGroup: String = project.group.toString()

/** This is defined above as `version`, is "1.0". */
val myArtifactVersion: String = project.version.toString()

/** My GitHub username. */
val myGithubUsername = "jakubDoka"
val myGithubDescription = "ANSI colored console log output"
val myGithubHttpUrl = "https://github.com/${myGithubUsername}/${myArtifactId}"
val myGithubIssueTrackerUrl = "https://github.com/${myGithubUsername}/${myArtifactId}/issues"
val myLicense = "Apache-2.0"
val myLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"

val myDeveloperName = "Jakub Dóka"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${myGithubUsername}/${myArtifactId}")
            credentials {
                username = System.getenv("GITHUB_PACKAGES_USERID")
                password = System.getenv("GITHUB_PACKAGES_PUBLISH_TOKEN")
            }
        }
    }
}

publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = myArtifactGroup
            artifactId = myArtifactId
            version = myArtifactVersion

            from(components["java"])

            pom {
                packaging = "jar"
                name.set(myArtifactId)
                description.set(myGithubDescription)
                url.set(myGithubHttpUrl)
                scm {
                    url.set(myGithubHttpUrl)
                }
                issueManagement {
                    url.set(myGithubIssueTrackerUrl)
                }
                licenses {
                    license {
                        name.set(myLicense)
                        url.set(myLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(myGithubUsername)
                        name.set(myDeveloperName)
                    }
                }
            }
        }
    }
}