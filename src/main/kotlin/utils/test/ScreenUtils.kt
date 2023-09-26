package utils.test

import core.DriverFactory
import io.appium.java_client.AppiumDriver
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.interactions.PointerInput
import org.openqa.selenium.interactions.PointerInput.Origin.viewport
import org.openqa.selenium.interactions.Sequence
import java.time.Duration.ofMillis

object ScreenUtils {
    private val logger: Logger = LogManager.getLogger(ScreenUtils::class.java)

    fun scrollDown() {
        scroll(0.8, 0.1)
    }

    fun swipeRight() {
        swipe(0.9, 0.2)
    }

    private fun swipe(start: Double, end: Double) {
        val driver = DriverFactory.getDriver()
        val dimension: Dimension = driver.manage().window().getSize()
        val moveTime = 100

        logger.info("Sliding from startY: $start to endY: $end")

        var start = Point((dimension.width * start).toInt(), (dimension.height / 2))
        var end = Point((dimension.width * end).toInt(), (dimension.height / 2))
        moveScreen(driver, start, end, moveTime)
    }

    fun scroll(start: Double, end: Double) {
        val driver = DriverFactory.getDriver()
        val dimension: Dimension = driver.manage().window().getSize()
        val moveTime = 100

        logger.info("Scrolling from startY: $start to endY: $end")

        var start = Point((dimension.width / 2), (dimension.height * start).toInt())
        var end = Point((dimension.width / 2), (dimension.height * end).toInt())
        moveScreen(driver, start, end, moveTime)
    }

    fun closeNotification() {
        val driver = DriverFactory.getDriver()
        val dimension: Dimension = driver.manage().window().getSize()
        val moveTime = 50

        var start = Point((dimension.width / 2), (dimension.height * 9 / 10))
        var end = Point((dimension.width / 2), (dimension.height / 10))

        moveScreen(driver, start, end, moveTime)
    }

    private fun moveScreen(driver: AppiumDriver, start: Point, end: Point, duration: Int) {
        val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
        val swipe = Sequence(finger, 1)
            .addAction(finger.createPointerMove(ofMillis(0), viewport(), start.getX(), start.getY()))
            .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
            .addAction(finger.createPointerMove(ofMillis(duration.toLong()), viewport(), end.getX(), end.getY()))
            .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()))
        driver.perform(listOf(swipe))
    }
}
