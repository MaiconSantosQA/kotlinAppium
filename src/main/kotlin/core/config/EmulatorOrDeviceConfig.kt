package core.config

import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.options.UiAutomator2Options
import java.net.URL
import java.nio.file.Paths

object EmulatorOrDeviceConfig {
    private var managerApk = ManagerApk()
    fun settingsCaps(deviceName: String?): AppiumDriver {
        val urlConnectionAppium = "http://localhost:4723/wd/hub"
        val filePath = Paths.get(managerApk.getApkResourceUrl().toURI()).toAbsolutePath().toString()
        val caps = UiAutomator2Options()
            .setDeviceName(deviceName)
            .setApp(filePath)
            .setNoReset(false)
            .autoGrantPermissions()
        caps.setCapability("appium:unicodeKeyboard", true)
        caps.setCapability("appium:resetKeyboard", true)
        caps.setCapability("appium:driverArgs", "--location -9.8302,-67.9518")

        return AppiumDriver(URL(urlConnectionAppium), caps)
    }
}
