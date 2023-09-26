package screen.principal

import core.DriverFactory
import core.config.selenium.ByApp
import core.config.selenium.WaitConfig
import org.openqa.selenium.By
import utils.test.InteractElementUtils

class HomeScreen {

    private val txtNotIfood = "iFood para Entregadores"
    private val btnProfile = ByApp.id("/home_profile_menu_item")
    private val msgAvaliable = ByApp.id("/homeStatusHeaderAvailableText")
    private val menu = By.xpath("//android.widget.ImageButton")
    private val homeCardTitle = ByApp.id("/homeCardTitleText")
    private val txtSkipRestrict = "Resolver depois"

    fun clickProfile() {
        InteractElementUtils.waitLoadElement(btnProfile)
        InteractElementUtils.toClick(btnProfile)
    }

    fun waitLoadHome() {
        InteractElementUtils.waitLoadElement(btnProfile)
    }

    fun clickMenu() {
        InteractElementUtils.toClick(menu)
    }

    fun moveCardRight() {
        val driver = DriverFactory.getDriver()

        val x = 888
        val y = 960
        InteractElementUtils.moveElementToXY(driver, homeCardTitle, x, y)
    }

    fun clickCardRoute() {
        val txtCard = "Ver rota"
        InteractElementUtils.clickByText(txtCard)
    }

    @Throws(RuntimeException::class, NoSuchElementException::class)
    fun validateLoginSucess() {
        InteractElementUtils.wait(homeCardTitle, WaitConfig.getTimeoutLongLoads())
    }

    fun verifyRestrict() {
        try {
            InteractElementUtils.clickByText(txtSkipRestrict)
        } catch (e: Exception) {
            println("Não apareceu informação de restrito!")
        }
    }
}
