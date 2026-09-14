package com.aetherlearn.app.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

object TermuxBridge {
    const val TERMUX_PACKAGE = "com.termux"
    const val RUN_COMMAND_ACTION = "com.termux.RUN_COMMAND"
    const val RUN_COMMAND_SERVICE = "com.termux.app.RunCommandService"
    const val EXTRA_COMMAND_PATH = "com.termux.RUN_COMMAND_PATH"
    const val EXTRA_ARGUMENTS = "com.termux.RUN_COMMAND_ARGUMENTS"
    const val EXTRA_WORKDIR = "com.termux.RUN_COMMAND_WORKDIR"
    const val EXTRA_BACKGROUND = "com.termux.RUN_COMMAND_BACKGROUND"
    const val EXTRA_SESSION_ACTION = "com.termux.RUN_COMMAND_SESSION_ACTION"

    fun isInstalled(context: Context): Boolean = runCatching {
        context.packageManager.getApplicationInfo(TERMUX_PACKAGE, PackageManager.GET_META_DATA)
        true
    }.getOrDefault(false)

    fun buildIntent(wrapper: TermuxExerciseWrapper): Intent {
        val request = TermuxWrapperRegistry.buildLaunchRequest(wrapper.wrapperId)
        TermuxWrapperRegistry.validate(request).getOrThrow()
        require(request.contractVersion == wrapper.contractVersion) { "Wrapper contract changed before handoff" }
        return Intent().apply {
            setClassName(TERMUX_PACKAGE, RUN_COMMAND_SERVICE)
            action = RUN_COMMAND_ACTION
            putExtra(EXTRA_COMMAND_PATH, request.executable)
            putExtra(EXTRA_ARGUMENTS, request.arguments.toTypedArray())
            putExtra(EXTRA_WORKDIR, request.workingDirectory)
            putExtra(EXTRA_BACKGROUND, false)
            putExtra(EXTRA_SESSION_ACTION, "0")
        }
    }

    fun launch(context: Context, wrapper: TermuxExerciseWrapper): TermuxLaunchResult {
        if (!isInstalled(context)) return TermuxLaunchResult.NotInstalled
        return runCatching {
            context.startService(buildIntent(wrapper))
            TermuxLaunchResult.Started
        }.getOrElse { failure ->
            when (failure) {
                is SecurityException -> TermuxLaunchResult.PermissionRequired
                is IllegalArgumentException -> TermuxLaunchResult.NotSupported
                else -> TermuxLaunchResult.Failed(failure.message ?: "Termux could not start the local exercise.")
            }
        }
    }
}

sealed interface TermuxLaunchResult {
    data object Started : TermuxLaunchResult
    data object NotInstalled : TermuxLaunchResult
    data object PermissionRequired : TermuxLaunchResult
    data object NotSupported : TermuxLaunchResult
    data class Failed(val message: String) : TermuxLaunchResult
}
