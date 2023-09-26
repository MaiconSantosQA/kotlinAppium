package utils.test

import core.config.selenium.ByApp
import org.openqa.selenium.By

object ClickUtils {

    private val btnRouteAction: By = ByApp.id("/routeActionButton")
    private const val btnConfirmExit = "Sim, confirmar saída"
    private const val btnConfirmArrival = "Sim, confirmar chegada"
    private const val btnExitDelivery = "Saí da entrega"

    fun nextInfoNewApp() {
        val clickNumber = 4
        next(clickNumber)
    }

    private fun next(clickNumber: Int) {
        try {
            for (i in 0 until clickNumber) {
                InteractElementUtils.clickByText("Próximo")
            }
        } catch (e: Exception) {
        }
    }

    fun confirm() {
        InteractElementUtils.clickByText("Confirmar")
    }

    fun ok() {
        try {
            InteractElementUtils.clickByText("Ok, entendi")
        } catch (e: Exception) {
        }
    }

    fun pressRouteAction() {
        InteractElementUtils.toPress(btnRouteAction)
    }

    fun clickConfirmExit() {
        InteractElementUtils.clickByText(btnConfirmExit)
    }

    fun clickArrivalExit() {
        InteractElementUtils.clickByText(btnExitDelivery)
    }

    fun clickConfirmArrival() {
        InteractElementUtils.clickByText(btnConfirmArrival)
    }

    fun clickBack() {
        InteractElementUtils.navigateBack()
    }
}
