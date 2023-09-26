package utils.test

import core.DriverFactory
import org.openqa.selenium.html5.Location
import org.openqa.selenium.html5.LocationContext

object LocationUtils {
    fun setLocation(latitude: Double, longitude: Double, altitude: Double) {
        val driver = DriverFactory.getDriver()

        if (driver is LocationContext) {
            val location = Location(latitude, longitude, altitude)
            driver.setLocation(location)
        } else {
            throw UnsupportedOperationException("Driver does not support location setting.")
        }
    }
}
