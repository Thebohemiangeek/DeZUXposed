package io.github.miner7222.dezux

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FeaturePolicyTest {

    private fun policyWith(vararg dirs: String) = FeaturePolicy { it in dirs.toSet() }

    @Test
    fun forcedFeaturesFollowTheLists() {
        val policy = policyWith()
        assertEquals(true, policy.decide("ZUISetupWizardExtROW"))
        assertEquals(false, policy.decide("BaiduNetworkLocation"))
    }

    @Test
    fun promoAppsAreSwitchedOff() {
        val policy = policyWith()
        assertEquals(false, policy.decide("LenovoWhatsNew"))
        assertEquals(false, policy.decide("TabletPushOut"))
        assertEquals(false, policy.decide("XuiEasySync"))
    }

    @Test
    fun unknownFeaturesKeepFirmwareValue() {
        assertNull(policyWith().decide("SomeFutureFeature"))
    }

    @Test
    fun coreScreensStayWithoutReplacement() {
        val policy = policyWith()
        assertNull(policy.decide("ZuiPackageInstaller"))
        assertNull(policy.decide("ZuiPermissionController"))
        assertNull(policy.decide("ZuiContacts"))
    }

    @Test
    fun coreScreensSwitchOffWhenReplacementInstalled() {
        val policy = policyWith(
            "/system/priv-app/GooglePackageInstaller",
            "/product/priv-app/GooglePermissionControllerPrebuilt",
        )
        assertEquals(false, policy.decide("ZuiPackageInstaller"))
        assertEquals(false, policy.decide("ZuiPermissionController"))
        assertNull(policy.decide("ZuiContacts"))
    }

    @Test
    fun replaceableFeaturesAreNotAlsoInTheFixedLists() {
        for (feature in HookConstants.REPLACEABLE_FEATURES.keys) {
            assertFalse(feature, feature in HookConstants.DISABLED_FEATURES)
            assertFalse(feature, feature in HookConstants.ENABLED_FEATURES)
        }
        assertTrue(HookConstants.ENABLED_FEATURES.intersect(HookConstants.DISABLED_FEATURES).isEmpty())
    }
}
