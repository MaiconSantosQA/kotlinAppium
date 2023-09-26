package core.config

import core.DriverFactory
import core.config.selenium.Environment
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.options.UiAutomator2Options
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.json.JSONObject
import org.openqa.selenium.remote.SessionId
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.Base64

object DeviceFarmConfig {
    val dotenv: Environment = Environment.create()
    private val logger: Logger = LogManager.getLogger()
    private val username = dotenv.get("BROWSERSTACK_USERNAME").toString()
    private val accessKey = dotenv.get("BROWSERSTACK_ACCESS_KEY").toString()
    private val build: String? = dotenv.get("NAME_BUILD").toString()
    private var executionBS = dotenv.get("BROWSERSTACK")
    private var device = dotenv.get("DEVICE_NAME")
    private var versionAndroid = dotenv.get("VERSION_ANDROID")
    private var managerApk = ManagerApk()

    private fun getSessionIDDriverDeviceFarm(): SessionId? {
        return DriverFactory.getDriver().sessionId
    }

    fun settingsCaps(): AppiumDriver {
        val url = "https://%s:%s@hub.browserstack.com/wd/hub"
        val options = UiAutomator2Options()
        options.setCapability("appium:app", uploadAPK())
        options.setCapability("appium:deviceName", device)
        options.setCapability("appium:platformVersion", versionAndroid)
        options.setCapability("interactiveDebugging", true)
        options.setCapability("project", "Ifood Driver App")
        options.setCapability("build", build)
        options.setCapability("autoGrantPermissions", true)
        options.setCapability("appium:unicodeKeyboard", true)
        options.setCapability("appium:resetKeyboard", true)
        options.setCapability("appium:noReset", false)
        options.setCapability("browserstack.parallelsPerPlatform", 3)
        options.setCapability("location", "true")
        options.setCapability("camera", "true")
        options.setCapability("microphone", "true")
        options.setCapability("contacts", "true")
        options.setCapability("calendar", "true")
        options.setCapability("sms", "true")
        options.setCapability("storage", "true")
        options.setCapability("browserstack.gpsLocation", "25.7758,-80.2126")
        options.setCapability("browserstack.appium_version", "1.22.0")

        val hubUrl = URL(url.format(username, accessKey))
        return AppiumDriver(hubUrl, options)
    }

    @Throws(IOException::class)
    fun uploadFileToDeviceFarm(file: File): Response {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", file.name,
                RequestBody.create(MediaType.parse("application/octet-stream"), file)
            )
            .build()
        val request = Request.Builder()
            .url("https://api-cloud.browserstack.com/app-automate/upload")
            .addHeader("Authorization", Credentials.basic(username, accessKey))
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        return client.newCall(request).execute()
    }

    private fun uploadAPK(): String {
        return try {
            val apkUrl = managerApk.getApkResourceUrl()
            val fileForUploading = managerApk.createTempFileForUploading(apkUrl)
            try {
                val uploadResponse = uploadFileToDeviceFarm(fileForUploading)
                getUrlFromUploadResponse(uploadResponse).also {
                    logger.info("Apk upload to browserstack successfully - $it")
                }
            } finally {
                managerApk.deleteTemporaryFileAndFolder(fileForUploading)
            }
        } catch (e: Exception) {
            logger.error("Failed to upload APK to browser stack: ${e.message}")
            throw e
        }
    }

    @Throws(RuntimeException::class)
    fun getUrlFromUploadResponse(response: Response): String {
        if (response.isSuccessful) {
            val jsonString = response.body()?.string().orEmpty()
            val jsonResponse = JSONObject(jsonString)

            if (jsonResponse.has("app_url")) {
                return jsonResponse.getString("app_url")
            } else {
                throw RuntimeException("Missing 'app_url' in upload response JSON")
            }
        } else {
            throw RuntimeException("Upload Request Failed - HTTP ${response.code()} ${response.message()}")
        }
    }

    fun updateDeviceFarmTestStatus(testName: String, status: String, reasonFailed: String?) {
        val sessionId = getSessionIDDriverDeviceFarm()
        val client = OkHttpClient()
        val url = "https://api-cloud.browserstack.com/app-automate/sessions/$sessionId.json"

        logger.info("Session id Browserstack: $sessionId")

        val json = JSONObject()
        json.put("status", status)
        json.put("name", testName)
        if (reasonFailed != null) {
            json.put("reason", reasonFailed)
        }
        val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString("$username:$accessKey".toByteArray()))
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            DriverFactory.driverShutdown()
            throw RuntimeException("Failed to update test status: ${response.code()} ${response.body()?.string()}")
        }
    }

    fun updateDeviceFarmTestName(testName: String) {

        if (executionBS!!.trim().equals("true", ignoreCase = true)) {
            val sessionId = getSessionIDDriverDeviceFarm()
            val client = OkHttpClient()
            val url = "https://api-cloud.browserstack.com/app-automate/sessions/$sessionId.json"

            logger.info("Session id Browserstack: $sessionId")

            val json = JSONObject()
            json.put("name", testName)
            val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())

            val request = Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader(
                    "Authorization",
                    "Basic " + Base64.getEncoder().encodeToString("$username:$accessKey".toByteArray())
                )
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                DriverFactory.driverShutdown()
                throw RuntimeException("Failed to update test status: ${response.code()} ${response.body()?.string()}")
            }
        }
    }
}
