package core.config.selenium

interface Environment {
    fun get(key: String): String?

    fun getOrDefault(key: String, defaultValue: String): String {
        return get(key) ?: defaultValue
    }

    companion object {
        fun create(): Environment {
            return DotEnvEnvironment()
        }
    }
}
