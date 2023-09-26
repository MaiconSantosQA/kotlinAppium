package features.auth.forgotpassword

import core.config.BaseIntegrationTest
import core.config.selenium.Environment
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import screen.NewAcessAccountScreen
import screen.WelcomeScreen
import screen.login.ForgotPasswordScreen
import screen.login.LoginScreen
import screen.login.PasswordScreen
import utils.test.LoginTest
import utils.test.PasswordResetTest
import utils.test.SanityTest
import utils.test.SmokeTest
import utils.test.ValidationElementUtils
import utils.users.EnvTestUserStore

@LoginTest
@PasswordResetTest
class ForgotPasswordTest : BaseIntegrationTest() {
    private val userStore = EnvTestUserStore(Environment.create())
    private val welcome: WelcomeScreen = WelcomeScreen()
    private val newsAccount: NewAcessAccountScreen = NewAcessAccountScreen()
    private val loginScreen: LoginScreen = LoginScreen()
    private val passwordScreen: PasswordScreen = PasswordScreen()
    private val forgotPass: ForgotPasswordScreen = ForgotPasswordScreen()

    private val txtTittleEmail: String = "E-mail"
    private val txtTittleSMS: String = "SMS"
    private val txtTittleWhatsapp: String = "WhatsApp"
    private val txtTypeDescriptionEmail: String = "Enviaremos um e-mail com o link para o endereço cadastrado."
    private val txtTypeDescriptionSMS: String = "Enviaremos uma mensagem SMS com o link para o número cadastrado."
    private val txtTypeDescriptionWhatsapp: String = "Enviaremos uma mensagem no WhatsApp com o link para o número cadastrado."
    private val txtMsgSucess: String = "Enviamos o link para alterar a senha, ele é válido por 15 minutos"

    private fun givenRequestedPasswordResetForUserWith(email: String) {
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(email)
        loginScreen.clickContinue()
        passwordScreen.clickForgotPassword()
    }

    @Test
    @SmokeTest
    fun `GIVEN approved user WHEN I request password reset via Email THEN should indicate that link was sent if the user exists`() {
        givenRequestedPasswordResetForUserWith(userStore.getTestDriver("APPROVED").email)
        assertTrue(ValidationElementUtils.searchText(txtTittleEmail))
        assertTrue(ValidationElementUtils.searchText(txtTypeDescriptionEmail))
        forgotPass.clickEmail()
        assertTrue(ValidationElementUtils.searchText(txtMsgSucess))
    }

    @Test
    @SanityTest
    fun `GIVEN approved user WHEN I request password reset via SMS THEN should indicate that link was sent if the user exists`() {
        givenRequestedPasswordResetForUserWith(userStore.getTestDriver("APPROVED").email)
        assertTrue(ValidationElementUtils.searchText(txtTittleSMS))
        assertTrue(ValidationElementUtils.searchText(txtTypeDescriptionSMS))
        forgotPass.clickSMS()
        assertTrue(ValidationElementUtils.searchText(txtMsgSucess))
    }

    @Test
    @SmokeTest
    @Disabled("Waiting for API to fix this")
    fun `GIVEN onboarding user WHEN I request password reset via Email THEN should indicate that link was sent if the user exists`() {
        givenRequestedPasswordResetForUserWith(userStore.getTestDriver("ONBOARDING").email)
        assertTrue(ValidationElementUtils.searchText(txtTittleEmail))
        assertTrue(ValidationElementUtils.searchText(txtTypeDescriptionEmail))
        forgotPass.clickEmail()
        assertTrue(ValidationElementUtils.searchText(txtMsgSucess))
    }

    @Test
    @Disabled("Waiting on API to fix this")
    fun `GIVEN driver has been removed WHEN attempting forgot password flow THEN should simulate sending link as if user existed`() {
        givenRequestedPasswordResetForUserWith(userStore.getTestDriver("REMOVED").email)
        assertTrue(ValidationElementUtils.searchText(txtTittleEmail))
        assertTrue(ValidationElementUtils.searchText(txtTypeDescriptionEmail))
        forgotPass.clickEmail()
        assertTrue(ValidationElementUtils.searchText(txtMsgSucess))
    }

    @Test
    @SanityTest
    @Disabled("Waiting on API to fix this")
    fun `GIVEN invalid user WHEN I request password reset via Email THEN should indicate that link was sent if the user exists`() {
        givenRequestedPasswordResetForUserWith("invalid_driver_email@invalid.com.br")
        assertTrue(ValidationElementUtils.searchText(txtTittleEmail))
        assertTrue(ValidationElementUtils.searchText(txtTypeDescriptionEmail))
        forgotPass.clickEmail()
        assertTrue(ValidationElementUtils.searchText(txtMsgSucess))
    }
}
