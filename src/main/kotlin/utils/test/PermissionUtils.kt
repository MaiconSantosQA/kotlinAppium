package utils.test

import core.DriverFactory
import org.openqa.selenium.By

object PermissionUtils {

    private const val idBtnSystemAllow = "com.android.packageinstaller:id/permission_allow_button"
    private var androidVersion: String? = DriverFactory.dotenv.get("VERSION_ANDROID")

    fun clickAllowPermission() {
        InteractElementUtils.waitLoadElement(By.id(idBtnSystemAllow))
        InteractElementUtils.toClick(By.id(idBtnSystemAllow))
    }

    fun verifyClickAllow() {
        val buttonText = when (androidVersion) {
            "8.0" -> "ALLOW"
            "6.0", "7.0" -> "YES"
            else -> "Allow"
        }

        InteractElementUtils.clickByTextSafe(buttonText)
    }
}
