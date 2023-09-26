package utils.test

import core.DriverFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.jupiter.api.fail
import org.openqa.selenium.By

object ValidationElementUtils {
    private val logger: Logger = LogManager.getLogger(ValidationElementUtils::class.java)

    fun elementIsVisibleSafe(by: By): Boolean {
        return try {
            logger.info("Checking if the element is visible: $by")
            InteractElementUtils.waitLoadElement(by)
            InteractElementUtils.findElement(by).isDisplayed
        } catch (e: Exception) {
            false
        }
    }

    fun elementIsVisible(by: By) {
        try {
            logger.info("Checking if the element is visible: $by")
            InteractElementUtils.findElement(by).isDisplayed
        } catch (e: Exception) {
            fail("Element not is visible: $by")
        }
    }

    fun elementIsEnabled(by: By): Boolean {
        logger.info("Checking if the element is enabled: $by")
        return InteractElementUtils.findElement(by).isEnabled
    }

    enum class Attribute(val value: String) {
        LONG_CLICKABLE("long-clickable"),
        CHECKED("checked"),
        SELECTED("selected")
    }

    fun getAttributeLongClickable(by: By): Boolean? {
        return getAtributeElement(by, Attribute.LONG_CLICKABLE)
    }

    fun getAttributeChecked(by: By): Boolean? {
        return getAtributeElement(by, Attribute.CHECKED)
    }

    fun getAttributeSelect(by: By): Boolean? {
        return getAtributeElement(by, Attribute.SELECTED)
    }

    private fun getAtributeElement(by: By, attribute: Attribute): Boolean? {
        logger.info("Getting the ${attribute.value} attribute of the element: $by")
        return InteractElementUtils.findElement(by).getAttribute(attribute.toString())?.toBoolean()
    }

    fun searchText(text: String): Boolean {
        val elementos = DriverFactory.getDriver().findElements(By.xpath("//*[contains(@text, '$text')]"))
        logger.info("Searching the text: $text")
        if (elementos.isEmpty()) {
            fail("Text not found: $text")
        }
        return elementos.size> 0
    }
}
