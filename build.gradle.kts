import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

group = "com.example"
version = "0.0.2"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC")
    implementation("me.tongfei:progressbar:0.9.2")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest.attributes["Main-Class"] = "com.example.filestorage.FileStorageApplicationKt"
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}