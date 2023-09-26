package utils.users

interface TestUserStore {
    fun getTestDriver(vararg tags: String): TestUser
}

data class TestUser(
    val email: String,
    val phone: String,
    val uuid: String,
    val password: String
)
