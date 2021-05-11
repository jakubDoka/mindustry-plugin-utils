plugins {
    java
    kotlin("jvm") version "1.4.31"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.10.2"
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
        kotlinOptions.jvmTarget = "1.8"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
    archiveClassifier.set("javadoc")
}

// More info on `publishing`:
//   https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:resolved_dependencies
// More info on authenticating with personal access token (myDeveloperId and myArtifactName must be lowercase):
//   https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#authenticating-to-github-packages
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

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

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