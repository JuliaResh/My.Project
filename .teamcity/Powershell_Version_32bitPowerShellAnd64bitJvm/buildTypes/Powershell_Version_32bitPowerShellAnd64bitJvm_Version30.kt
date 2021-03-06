package Powershell_Version_32bitPowerShellAnd64bitJvm.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.PowerShellStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.BuildFailureOnText
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.BuildFailureOnText.*
import jetbrains.buildServer.configs.kotlin.v10.failureConditions.failOnText

object Powershell_Version_32bitPowerShellAnd64bitJvm_Version30 : BuildType({
    uuid = "f8e2b50c-0cae-40fc-b4de-3d7d9afaa35f"
    extId = "Powershell_Version_32bitPowerShellAnd64bitJvm_Version30"
    name = "Version 3.0"
    paused = true


    vcs {
        root(PowerShell.vcsRoots.Powershell_PowerShell)

        checkoutMode = CheckoutMode.ON_SERVER
    }

    steps {
        powerShell {
            name = "External file"
            minVersion = PowerShellStep.Version.v3_0
            scriptMode = file {
                path = "host.ps1"
            }
            noProfile = false
            param("jetbrains_powershell_script_code", "Write-Output ${'$'}host")
        }
        powerShell {
            name = "External file with arguments"
            minVersion = PowerShellStep.Version.v3_0
            scriptMode = file {
                path = "host.ps1"
            }
            noProfile = false
            param("jetbrains_powershell_scriptArguments", "arg1 arg2 arg3")
            param("jetbrains_powershell_script_code", "Write-Output ${'$'}host")
        }
        powerShell {
            name = "Source code"
            minVersion = PowerShellStep.Version.v3_0
            scriptMode = script {
                content = """
                    ${'$'}PSVersionTable
                    If ([System.IntPtr]::Size -eq 4) { "32-bit" } ElseIf ([System.IntPtr]::Size -eq 8) { "64-bit" } Else {"smth wrong"}
                """.trimIndent()
            }
            noProfile = false
        }
    }

    failureConditions {
        failOnText {
            conditionType = BuildFailureOnText.ConditionType.REGEXP
            pattern = """-Version\,? 3\.0"""
            failureMessage = "version 3.0 was not specified when launching powershell"
            reverse = true
        }
        failOnText {
            conditionType = BuildFailureOnText.ConditionType.REGEXP
            pattern = """-Version\,? 3\.0.* arg1\,? arg2\,? arg3"""
            reverse = true
        }
        failOnText {
            conditionType = BuildFailureOnText.ConditionType.CONTAINS
            pattern = "64-bit"
            failureMessage = "failed to launch 32-bit PowerShell"
            reverse = false
        }
    }

    requirements {
        contains("teamcity.agent.jvm.os.arch", "64")
    }
})
