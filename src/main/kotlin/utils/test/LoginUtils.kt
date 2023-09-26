package utils.test

import screen.NewAcessAccountScreen
import screen.WelcomeScreen
import screen.login.LoginScreen
import screen.login.PasswordScreen
import screen.principal.HomeScreen

object LoginUtils {

    private val welcome = WelcomeScreen()
    private val newsAccount = NewAcessAccountScreen()
    private val loginScreen = LoginScreen()
    private val passwordScreen = PasswordScreen()
    private val home = HomeScreen()

    fun login(email: String, password: String) {
        welcome.clickEnter()
        newsAccount.clickExit()
        loginScreen.fillEmail(email)
        loginScreen.clickContinue()
        passwordScreen.fillPassword(password)
    }
}
