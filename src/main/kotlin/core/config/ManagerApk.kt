package core.config

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class ManagerApk {

    private val logger: Logger = LogManager.getLogger()

    fun getApkResourceUrl(): URL {
        val resourcePath = javaClass.classLoader.getResource("apk")?.path ?: throw IllegalStateException("APK directory not found")
        val apkDirectory = File(resourcePath)

        val apkFiles = apkDirectory.listFiles { file ->
            file.isFile && file.extension == "apk"
        }

        requireNotNull(apkFiles) { "No APK files found in the directory" }
        require(apkFiles.isNotEmpty()) { "No APK files found in the directory" }

        return apkFiles[0].toURI().toURL()
    }

    @Throws(IOException::class)
    fun createTempFileForUploading(apkUrl: URL): File {
        val tempDirectory = Files.createTempDirectory("temp_apk_directory")
        val tempFile = tempDirectory.resolve("app-prod-internalBeta.apk").toFile()
        apkUrl.openStream().use {
            Files.copy(it, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        return tempFile
    }

    fun deleteTemporaryFileAndFolder(tempFile: File) {
        try {
            val tempFolder = tempFile.parentFile
            tempFile.delete()
            tempFolder?.delete()
        } catch (ex: IOException) {
            logger.warn("Failed to delete temporary file: ${ex.message}, ignoring problem")
        }
    }
}
