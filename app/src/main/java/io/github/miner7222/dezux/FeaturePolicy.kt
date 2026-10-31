package io.github.miner7222.dezux

import java.io.File

/**
 * Decides the value LgsiFeatures.enabled() should report for a ZUI feature.
 *
 * Some ZUI features *are* the device's only implementation of a core screen
 * (on the TB322FC, com.android.packageinstaller lives in ZuiPackageInstaller).
 * Those are only switched off when the Google replacement is installed,
 * e.g. by the ZUXFree add-on or DeZUX; otherwise the stock value is kept.
 */
internal class FeaturePolicy(
    private val dirExists: (String) -> Boolean = { File(it).isDirectory },
) {
    private val replacementPresent = HashMap<String, Boolean>()

    /** true/false to override, or null to keep the firmware's own value. */
    fun decide(feature: String): Boolean? = when (feature) {
        in HookConstants.ENABLED_FEATURES -> true
        in HookConstants.REPLACEABLE_FEATURES -> if (hasReplacement(feature)) false else null
        in HookConstants.DISABLED_FEATURES -> false
        else -> null
    }

    private fun hasReplacement(feature: String): Boolean = synchronized(replacementPresent) {
        replacementPresent.getOrPut(feature) {
            val names = HookConstants.REPLACEABLE_FEATURES.getValue(feature)
            names.any { name -> PARTITIONS.any { dirExists("$it/$name") } }
        }
    }

    private companion object {
        val PARTITIONS = listOf(
            "/system/priv-app", "/system/app",
            "/product/priv-app", "/product/app",
            "/system_ext/priv-app", "/system_ext/app",
        )
    }
}
