package utils.test

import core.DriverFactory
import core.config.selenium.WaitConfig.getImplicitWaitTimeout
import io.appium.java_client.AppiumDriver
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration.ofSeconds

object InteractElementUtils {
    private val logger: Logger = LogManager.getLogger(InteractElementUtils::class.java)

    fun findElement(elementSelector: By): WebElement {

        val driver = DriverFactory.getDriver()

        return try {
            logger.info("Searching element: $elementSelector")
            waitLoadElement(elementSelector)
            driver.findElement(elementSelector)
        } catch (ex: Exception) {
            logger.error("Page element not found: $elementSelector")
            throw RuntimeException(ex)
        }
    }

    fun moveElementToXY(driver: AppiumDriver, origin: By, cordenateBX: Int, cordenateBY: Int) {
        val start = findElement(origin)
        val cordenateAX = start.location.x
        val cordenateAY = start.location.y
        pressAndMoveElement(driver, cordenateAX, cordenateAY, cordenateBX, cordenateBY)
    }

    private fun pressAndMoveElement(driver: AppiumDriver, startX: Int, startY: Int, endX: Int, endY: Int) {
        val actions = Actions(driver)

        try {
            val steps = 10
            val intervalMs = 200L
            logger.info("Pressing on the coordinates: ($startX, $startY) and moving to the coordinates: ($endX, $endY)")

            actions.moveByOffset(startX, startY).clickAndHold().perform()

            val stepX = (endX - startX) / steps
            val stepY = (endY - startY) / steps

            for (i in 0 until steps) {
                actions.moveByOffset(stepX, stepY).perform()
                Thread.sleep(intervalMs)
            }
        } catch (e: Exception) {
            logger.error("An error occurred while executing pressAndMoveElement: ${e.message}")
        } finally {
            actions.release().perform()
        }
    }

    fun fill(by: By, text: String?) {
        waitLoadElement(by)
        findElement(by).clear()
        findElement(by).sendKeys(text)
    }

    fun fillKeyboard(by: By, value: String) {
        val element = findElement(by)
        element.click()
        logger.info("Filling in the field: $by with the text: $value")
        element.sendKeys(value)
    }

    fun getText(by: By): String {
        waitLoadElement(by)
        logger.info("Getting the text from the field: $by")
        return findElement(by).text
    }

    fun toClick(by: By) {
        waitLoadElement(by)
        logger.info("Clicking the button: $by")
        findElement(by).click()
    }

    fun toPress(by: By) {
        val driver: WebDriver = DriverFactory.getDriver()
        val btnToPress = driver.findElement(by)

        val actions = Actions(driver)
        actions.clickAndHold(btnToPress)
            .pause(ofSeconds(1))
            .release()
            .perform()
    }

    fun clickByText(text: String) {
        waitLoadElement(By.xpath("//*[@text='$text']"))
        logger.info(
            "\n" +
                "Clicking on the text: $text"
        )
        toClick(By.xpath("//*[@text='$text']"))
    }

    fun clickByTextSafe(text: String) {
        try {
            toClick(By.xpath("//*[@text='$text']"))
        } catch (e: Exception) {
            logger.debug("The text did not appear")
        }
    }

    fun selectComb(by: By, valor: String) {
        logger.info(
            "Clicking on the combobox: $by \n" +
                "and selecting the text: $valor"
        )
        findElement(by).click()
        clickByText(valor)
    }

    fun waitLoadElement(by: By) {
        wait(by, getImplicitWaitTimeout())
    }

    fun wait(by: By, timeout: Long) {
        val driver = DriverFactory.getDriver()
        val wait = WebDriverWait(driver, ofSeconds(timeout))
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by))
    }

    fun navigateBack() {
        val driver = DriverFactory.getDriver()
        driver.navigate().back()
    }

    @Throws(RuntimeException::class)
    fun moveElementToElement(elementX: By, elementY: By) {
        val driver = DriverFactory.getDriver()
        val actions = Actions(driver)

        val elementX = findElement(elementX)
        val elementY = findElement(elementY)

        actions.dragAndDrop(elementX, elementY).build().perform()
    }
}
