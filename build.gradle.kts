
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    kotlin("jvm") version "1.8.21"
    application
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
    implementation("com.android.tools.build:gradle:7.0.4")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.json:json:20230227")
    implementation("commons-io:commons-io:2.11.0")
    implementation("io.rest-assured:rest-assured:5.3.0")
    implementation("org.assertj:assertj-core:3.24.2")
    implementation("org.hamcrest:hamcrest-core:2.2")
    implementation("org.json:json:20230227")

    implementation("org.seleniumhq.selenium:selenium-java:4.10.0")
    implementation("io.appium:java-client:8.3.0")
    implementation("org.seleniumhq.selenium:selenium-support:4.8.1")
    implementation("com.squareup.okhttp3:okhttp:3.0.0")

    implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("io.github.bonigarcia:webdrivermanager:5.4.1")
    implementation("org.yaml:snakeyaml:2.0")
}

tasks.test {
    useJUnitPlatform {
        val iTags = project.findProperty("includeTags") as? String
        val eTags = project.findProperty("excludeTags") as? String
        iTags?.let { includeTags = it.split(",").toSet() }
        eTags?.let { excludeTags = it.split(",").toSet() }
    }
    systemProperty("appium.server.url", "http://localhost:4723/wd/hub")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}

tasks.register<DownloadTask>("downloadFile") {
    val properties = Properties()
    val configFilePath = ".env"
    FileInputStream(configFilePath).use { fileInputStream ->
        properties.load(fileInputStream)
    }
    sourceUrl = properties.getProperty("URL_APK_DOWNLOAD", null)?.trim()
    target = File(project.rootDir, "src/test/resources/apk/app-prod-internalBeta.apk")
}

abstract class DownloadTask : DefaultTask() {
    @Input
    var sourceUrl: String? = null

    @OutputFile
    var target: File? = null

    init {
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun download() {
        if (!sourceUrl.isNullOrBlank()) {
            ant.withGroovyBuilder {
                "get"("src" to sourceUrl, "dest" to target, "usetimestamp" to "true")
            }
            logger.info("Download successfully: ${target?.absolutePath}")
        } else {
            logger.info("The download url was not defined, the file used will be the apk that is in the project.")
        }
    }
}

tasks.processTestResources {
    dependsOn("downloadFile")
}
