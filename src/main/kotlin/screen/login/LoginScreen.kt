package screen.login

import core.DriverFactory
import core.config.selenium.ByApp
import core.config.selenium.WaitConfig.getImplicitWaitTimeout
import org.apache.logging.log4j.LogManager
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.WebDriverWait
import utils.test.InteractElementUtils
import utils.test.ValidationElementUtils
import java.time.Duration

class LoginScreen {
    private val logger = LogManager.getLogger(LoginScreen::class.java)
    val toolbarTittle: By = ByApp.id("/toolbarTitle")
    val btnQuestion: By = ByApp.id("/otp_question_mark")
    val txtTittle: By = ByApp.id("/new_auth_identifier_title")
    val txtSubTittle: By = ByApp.id("/otp_identifier_container")
    val edtEmail: By = ByApp.id("/new_auth_identifier_input")
    val edtPhone: By = ByApp.id("/new_auth_identifier_phone_input")
    val cbxSaveEmail: By = ByApp.id("/new_auth_save_auth_type_checkBox")
    val btnContinue: By = ByApp.id("/new_auth_identifier_button")
    val btnEnterForPhone: By = ByApp.id("/new_auth_change_auth_type_button")
    val infoLogin: By = ByApp.id("/info_description_view")

    fun fillEmail(email: String?) {
        logger.info("login: $email")
        InteractElementUtils.fill(edtEmail, email)
    }

    fun fillPhone(phone: String?) {
        InteractElementUtils.fill(edtPhone, phone)
    }

    fun validateEmailFilled(email: String) {
        assert(InteractElementUtils.getText(edtEmail).contains(email))
    }

    fun clickContinue() {
        InteractElementUtils.toClick(btnContinue)
    }

    fun clickPhone() {
        InteractElementUtils.toClick(btnEnterForPhone)
    }

    fun validateLoginCompleted() {
        // TODO - Ask Maicon for help with this guy
        ValidationElementUtils.elementIsVisible(btnContinue)
        assert(!ValidationElementUtils.elementIsVisibleSafe(infoLogin))
        val waitDriver = WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(getImplicitWaitTimeout()))
        waitDriver.until { !ValidationElementUtils.elementIsVisibleSafe(btnContinue) }
    }
}
