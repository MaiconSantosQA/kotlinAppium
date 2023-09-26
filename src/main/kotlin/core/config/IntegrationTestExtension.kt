package core.config

import core.DriverFactory
import core.config.selenium.Environment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.jvm.optionals.getOrNull

class IntegrationTestExtension : AfterEachCallback, BeforeEachCallback {
    val dotenv = Environment.create()
    private val executionBS = dotenv.get("BROWSERSTACK")

    private val logger: Logger = LogManager.getLogger(IntegrationTestExtension::class.java)
    override fun afterEach(context: ExtensionContext) {
        val failure = context.executionException.getOrNull()
        val status = if (failure == null) "passed" else "failed"
        updateBsState(context.displayName, status, failure?.message)
        DriverFactory.driverShutdown()
    }

    override fun beforeEach(context: ExtensionContext) {
        DeviceFarmConfig.updateDeviceFarmTestName(context.displayName)
    }

    private fun updateBsState(testName: String, status: String, failureMessage: String?) {
        if (executionBS != null && executionBS.trim().equals("true", ignoreCase = true)) {
            logger.info("Recording test results in Browserstack")
            DeviceFarmConfig.updateDeviceFarmTestStatus(testName, status, failureMessage)
        }
    }
}
