package io.github.miner7222.dezux

internal class GlobalFeatureHooks(
    private val scope: ModernHookScope,
) {
    fun install() {
        scope.install("LgsiFeatures enabled hook") {
            replaceMethod("com.lgsi.config.LgsiFeatures", "enabled", String::class.java) {
                if (args.isEmpty()) return@replaceMethod callOriginal()
                val feature = args[0] as? String ?: return@replaceMethod callOriginal()
                policy.decide(feature) ?: callOriginal()
            }
        }
    }

    private companion object {
        // One policy per process, so replacement checks hit the disk once.
        val policy = FeaturePolicy()
    }
}
