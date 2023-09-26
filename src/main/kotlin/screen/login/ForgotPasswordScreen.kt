package screen.login

import utils.test.InteractElementUtils

class ForgotPasswordScreen {
    val txtBtnEmail: String = "E-mail"
    val txtBtnWhatsapp: String = "WhatsApp"
    val txtBtnSMS: String = "SMS"

    fun clickEmail() {
        InteractElementUtils.clickByText(txtBtnEmail)
    }

    fun clickWhatsApp() {
        InteractElementUtils.clickByText(txtBtnWhatsapp)
    }

    fun clickSMS() {
        InteractElementUtils.clickByText(txtBtnSMS)
    }
}
