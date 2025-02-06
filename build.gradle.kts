
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.sharma"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.mongodb.driver.core)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.bson)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    //Mongo DB
    // MongoDB Driver
    implementation("org.litote.kmongo:kmongo:5.2.0") // Kotlin MongoDB wrapper
    // MongoDB Reactive Streams Driver
    implementation("org.mongodb:mongodb-driver-reactivestreams:5.3.1")

    // KMongo (Optional, for better Kotlin support)
    implementation("org.litote.kmongo:kmongo-coroutine:5.2.0")

    implementation("commons-codec:commons-codec:1.16.0") // Latest version

    //call external api
    implementation("io.ktor:ktor-client-core:2.3.4")  // Ktor Client Core
    implementation("io.ktor:ktor-client-cio:2.3.4")   // HTTP Client (CIO Engine)
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4") // Content Negotiation

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
