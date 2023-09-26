package screen.login

import core.config.selenium.ByApp
import core.config.selenium.WaitConfig
import org.openqa.selenium.By
import utils.test.InteractElementUtils
import utils.test.ValidationElementUtils

class PasswordScreen {
    private val edtPassword: By = ByApp.id("/password_auth_insert_password_input")
    private val btnContinue: By = ByApp.id("/password_auth_insert_password_button")
    private val btnForgotPassword: By = ByApp.id("/password_auth_insert_password_forgot_password_text")
    private val msgError: By = ByApp.id("/info_description_view")

    fun fillPassword(password: String?) {
        InteractElementUtils.wait(edtPassword, WaitConfig.getTimeoutLongLoads())
        InteractElementUtils.fill(edtPassword, password)
        InteractElementUtils.toClick(btnContinue)
    }

    fun clickForgotPassword() {
        InteractElementUtils.toClick(btnForgotPassword)
    }

    fun getTxtMsgErrorEmailPassword(): String {
        return InteractElementUtils.getText(msgError)
    }

    fun validateElements() {
        ValidationElementUtils.elementIsVisible(edtPassword)
        ValidationElementUtils.elementIsVisible(btnContinue)
    }

    fun navigateBack() {
        InteractElementUtils.navigateBack()
    }
}
