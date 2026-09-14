package com.aetherlearn.app.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TermuxWrappersTest {
    @Test
    fun registryContainsOnlyTheSevenSelectedLocalExercises() {
        assertEquals(
            setOf(
                "py-02-local-expressions",
                "py-03-local-variables-output",
                "py-05-local-loop-trace",
                "py-06-local-functions",
                "py-07-local-data-summary",
                "dev-01-safe-navigation",
                "dev-02-local-git-version",
            ),
            TermuxWrapperRegistry.wrappers.map { it.wrapperId }.toSet(),
        )
        assertTrue(TermuxWrapperRegistry.validateRegistry().isEmpty())
        assertTrue(TermuxWrapperRegistry.wrappers.all { !it.networkRequired })
    }

    @Test
    fun unknownWrapperIdIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            TermuxWrapperRegistry.buildLaunchRequest("unknown-wrapper")
        }
        assertTrue(
            TermuxWrapperRegistry.validate(
                TermuxLaunchRequest(1, "unknown-wrapper", "/bin/echo", emptyList(), "/tmp"),
            ).isFailure,
        )
    }

    @Test
    fun alteredArgumentsAreRejected() {
        val request = TermuxWrapperRegistry.buildLaunchRequest("py-02-local-expressions")
        val altered = request.copy(arguments = request.arguments + "user input")
        assertTrue(TermuxWrapperRegistry.validate(altered).isFailure)
    }

    @Test
    fun alteredExecutableAndWorkingDirectoryAreRejected() {
        val request = TermuxWrapperRegistry.buildLaunchRequest("dev-01-safe-navigation")
        assertTrue(TermuxWrapperRegistry.validate(request.copy(executable = "/bin/sh")).isFailure)
        assertTrue(TermuxWrapperRegistry.validate(request.copy(workingDirectory = "/sdcard/Download")).isFailure)
    }

    @Test
    fun alteredContractVersionIsRejected() {
        val request = TermuxWrapperRegistry.buildLaunchRequest("dev-01-safe-navigation")
        assertTrue(TermuxWrapperRegistry.validate(request.copy(contractVersion = 99)).isFailure)
    }
}
