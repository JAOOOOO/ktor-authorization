import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val NEXUS_PASSWORD: String by project
val NEXUS_USERNAME: String by project

val ktorVersion: String by project
val groupId: String by project
val versionCode: String by project


plugins {
  kotlin("jvm") version "1.7.20"
  `java-library`
  `maven-publish`
  signing
}

group = groupId

version = versionCode

java {
  withJavadocJar()
  withSourcesJar()
}

repositories { mavenCentral() }

dependencies {
  testImplementation(kotlin("test"))

  api("io.ktor:ktor-server-core:$ktorVersion")
  api("io.ktor:ktor-server-auth:$ktorVersion")

  testImplementation("io.ktor:ktor-server-sessions:$ktorVersion")
  testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
  testImplementation("org.jetbrains.kotlin:kotlin-test:$1.7.20")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.0")
}

tasks.withType<JavaCompile> {
  sourceCompatibility = JavaVersion.VERSION_1_8.toString()
  targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.test { useJUnitPlatform() }

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifactId = "ktor-authorization"
      from(components["java"])
      pom {
        name.set("Ktor-Authorization")
        description.set("A lightweight plugin to ease role base authorization in Ktor")
        url.set("https://github.com/JAOOOOO/ktor-authorization")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }

        developers {
          developer {
            id.set("JalalOkbi")
            name.set("Jalal Okbi")
            email.set("jalalokbi.dev@gmail.com")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/JAOOOOO/ktor-authorization")
          developerConnection.set("scm:git:ssh://github.com/JAOOOOO/ktor-authorization")
          url.set("https://github.com/JAOOOOO/ktor-authorization")
        }

          repositories {
              maven {

                  credentials {
                      username = NEXUS_USERNAME
                      password = NEXUS_PASSWORD
                  }

                  name = "Ktor-Authorization"
                  url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
              }
          }
      }
    }
  }
}

signing {
    sign(publishing.publications["mavenJava"])
}
