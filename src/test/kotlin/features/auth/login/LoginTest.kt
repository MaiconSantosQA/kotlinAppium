package features.auth.login
import core.config.BaseIntegrationTest
import core.config.selenium.Environment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import screen.NewAcessAccountScreen
import screen.WelcomeScreen
import screen.login.LoginScreen
import screen.login.PasswordScreen
import screen.otp.OtpCodeScreen
import screen.otp.OtpScreen
import utils.test.LoginTest
import utils.test.SanityTest
import utils.test.SmokeTest
import utils.users.EnvTestUserStore

@LoginTest
class LoginTest : BaseIntegrationTest() {
    private val testUserStore = EnvTestUserStore(Environment.create())
    private val welcome = WelcomeScreen()
    private val newsAccount = NewAcessAccountScreen()
    private val loginScreen = LoginScreen()
    private val passwordScreen = PasswordScreen()
    private val otpScreen = OtpScreen()
    private val otpCode = OtpCodeScreen()

    private var email: String? = null
    private var password: String? = null

    @Test
    @SmokeTest
    fun `GIVEN approved user WHEN I login THEN should complete it normally`() {
        val user = testUserStore.getTestDriver("APPROVED")
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(user.email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(user.password)
        loginScreen.validateLoginCompleted()
    }

    @Test
    @SmokeTest
    fun `GIVEN approved user WHEN I attempt login using phone as identifier SHOULD complete normally`() {
        val user = testUserStore.getTestDriver("APPROVED")
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.clickPhone()
        loginScreen.fillPhone(user.phone)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(user.password)
        loginScreen.validateLoginCompleted()
    }

    @Test
    @SanityTest
    fun `GIVEN approved user WHEN I insert the wrong password THEN should show the correct error`() {
        email = testUserStore.getTestDriver("APPROVED").email
        password = "QWERTY"
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(password)
        assertEquals("Email/telefone ou senha está errado", passwordScreen.getTxtMsgErrorEmailPassword())
    }

    @Test
    @SanityTest
    fun `GIVEN approved user WHEN I drop off the login flow mid password input and resume THEN should complete login where I left off`() {
        val user = testUserStore.getTestDriver("APPROVED")
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(user.email)
        loginScreen.clickContinue()
        passwordScreen.validateElements()
        passwordScreen.navigateBack()
        loginScreen.validateEmailFilled(user.email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(user.password)
        loginScreen.validateLoginCompleted()
    }

    @Test
    @Disabled("Currently we are unable to actually recover the OTP code to actually finish the login flow")
    @SmokeTest
    fun `GIVEN approved user which requires OTP step WHEN login in SHOULD complete flow normally`() {
        val code = "123456"
        val user = testUserStore.getTestDriver("APPROVED", "OTP")
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(user.email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(user.password)
        otpScreen.clickEmail()
        otpCode.fillCode(code)
        otpCode.clickEnter()
        loginScreen.validateLoginCompleted()
    }

    @Test
    @Disabled("Currently we are unable to actually recover the OTP code to actually finish the login flow")
    @SanityTest
    fun `GIVEN approved user WHEN I drop off the login flow mid otp input and resume THEN should complete login where I left off`() {
        val user = testUserStore.getTestDriver("APPROVED", "OTP")
        val code = "123456"
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(user.email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(user.password)
        otpScreen.validateElements()
        otpScreen.navigateBack()
        loginScreen.validateEmailFilled(user.email)
        loginScreen.clickContinue()
        otpScreen.clickEmail()
        otpCode.validateElements()
        otpCode.navigateBack()
        otpScreen.validateElements()
        otpScreen.navigateBack()
        loginScreen.validateEmailFilled(user.email)
        loginScreen.clickContinue()
        otpScreen.clickEmail()
        otpCode.fillCode(code)
        otpCode.clickEnter()
        loginScreen.validateLoginCompleted()
    }

    @Test
    @SanityTest
    fun `GIVEN user removed WHEN attempting to login THEN should fail with invalid user or password error`() {
        val user = testUserStore.getTestDriver("REMOVED")
        email = user.email
        password = user.password
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(password)
        assertEquals("Email/telefone ou senha está errado", passwordScreen.getTxtMsgErrorEmailPassword())
    }
}
