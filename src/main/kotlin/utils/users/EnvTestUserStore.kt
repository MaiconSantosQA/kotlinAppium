package utils.users

import core.config.selenium.Environment

class EnvTestUserStore(
    val environment: Environment
) : TestUserStore {
    override fun getTestDriver(vararg tags: String): TestUser {
        val tagKey = tags.joinToString(separator = "_") { it.uppercase() }
        return TestUser(
            email = getDriverDataFromEnv("EMAIL", tagKey),
            phone = getDriverDataFromEnv("PHONE", tagKey),
            uuid = getDriverDataFromEnv("UUID", tagKey),
            password = environment.get("DRIVER_PASSWORD_TEST").orEmpty()
        )
    }

    private fun getDriverDataFromEnv(type: String, tagsKey: String): String {
        return environment.get("DRIVER_${type}_TEST_$tagsKey").toString()
    }
}
