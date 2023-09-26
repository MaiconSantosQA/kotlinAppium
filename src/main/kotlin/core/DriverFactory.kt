package core

import core.config.DeviceFarmConfig
import core.config.EmulatorOrDeviceConfig
import core.config.selenium.Environment
import core.config.selenium.WaitConfig.getImplicitWaitTimeout
import io.appium.java_client.AppiumDriver
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.TimeoutException
import java.net.MalformedURLException
import java.time.Duration

object DriverFactory {

    private val logger: Logger = LogManager.getLogger(DriverFactory::class.java)
    val dotenv: Environment = Environment.create()
    private val environment: String? = dotenv.get("BROWSERSTACK").toString()
    private val deviceName: String? = dotenv.get("DEVICE_NAME")
    private var driver: AppiumDriver? = null

    fun getDriver(): AppiumDriver {
        if (driver == null) {
            logger.info("Create driver!")
            createDriver()
        }
        return driver!!
    }

    fun restartApp() {
        driver?.quit()

        if (environment!!.trim() == "false") {
            try {
                driver = EmulatorOrDeviceConfig.settingsCaps(deviceName)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        } else {
            driver = DeviceFarmConfig.settingsCaps()
        }
        driver!!.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitWaitTimeout()))
    }

    private fun createDriver() {

        if (environment!!.trim() == "false") {
            try {
                driver = EmulatorOrDeviceConfig.settingsCaps(deviceName)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        } else {
            driver = DeviceFarmConfig.settingsCaps()
        }

        try {
            driver!!.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitWaitTimeout()))
        } catch (e: TimeoutException) {
            logger.error("Connection Timeout: Check VPN!")
            e.printStackTrace()
        }
    }

    fun driverShutdown() {
        if (driver != null) {
            driver!!.quit()
            driver = null
        }
    }
}
