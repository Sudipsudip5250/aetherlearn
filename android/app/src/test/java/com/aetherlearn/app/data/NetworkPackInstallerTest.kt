package com.aetherlearn.app.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkPackInstallerTest {
    @Test
    fun acceptsHttpsUrlWithoutCredentialsOrFragment() {
        assertEquals(
            "https://example.org/packs/core.zip?channel=stable",
            NetworkPackInstaller.validateUrl(" https://example.org/packs/core.zip?channel=stable "),
        )
    }

    @Test
    fun rejectsCleartextCredentialsAndFragments() {
        assertFailsWith<IllegalArgumentException> { NetworkPackInstaller.validateUrl("http://example.org/pack.zip") }
        assertFailsWith<IllegalArgumentException> { NetworkPackInstaller.validateUrl("https://user:secret@example.org/pack.zip") }
        assertFailsWith<IllegalArgumentException> { NetworkPackInstaller.validateUrl("https://example.org/pack.zip#lesson") }
    }

    @Test
    fun acceptsOnlySafeModulePaths() {
        assertTrue(NetworkPackInstaller.isSafeModulePath("lesson-01.md"))
        assertFalse(NetworkPackInstaller.isSafeModulePath("modules/../secrets.txt"))
        assertFalse(NetworkPackInstaller.isSafeModulePath("modules/subdir/lesson.md"))
        assertFalse(NetworkPackInstaller.isSafeModulePath("/tmp/lesson.md"))
        assertFalse(NetworkPackInstaller.isSafeModulePath("lesson name.md"))
    }

    @Test
    fun followsCanonicalRemotePackCurriculumIds() {
        assertTrue(NetworkPackInstaller.isApprovedCurriculumId("dev-03-debugging-error-messages"))
        assertFalse(NetworkPackInstaller.isApprovedCurriculumId("dev-03-debugging-errors-reproduction"))
        assertFalse(NetworkPackInstaller.isApprovedCurriculumId("dl-06-computing-language-history"))
        assertFalse(NetworkPackInstaller.isApprovedCurriculumId("dl-08-networks-web-concepts"))
    }
}
