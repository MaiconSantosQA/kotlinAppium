package core.config.selenium

object WaitConfig {
    val dotenv: Environment = Environment.create()
    private val deviceFarm: String? = dotenv.get("BROWSERSTACK").toString().trim()?.lowercase()

    fun getImplicitWaitTimeout(): Long {
        return when (deviceFarm) {
            "true" -> 8L
            else -> 5L
        }
    }

    fun getTimeoutLongLoads(): Long {
        return 10L
    }

    fun getTimeLimitDisplayDelivery(): Long {
        return 30L
    }
}
