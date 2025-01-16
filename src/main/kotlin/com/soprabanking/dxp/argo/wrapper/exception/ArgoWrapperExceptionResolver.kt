package com.soprabanking.dxp.argo.wrapper.exception

import org.slf4j.LoggerFactory
import org.springframework.shell.command.CommandExceptionResolver
import org.springframework.shell.command.CommandHandlingResult
import org.springframework.stereotype.Component

@Component
class ArgoWrapperExceptionResolver : CommandExceptionResolver {

    private val logger = LoggerFactory.getLogger(ArgoWrapperExceptionResolver::class.java)

    override fun resolve(e: Exception): CommandHandlingResult? {
        return when (e) {
            is WrapperException -> e.logs + listOf(
                "ERROR: ${e.message}",
                " Command: ${e.command.joinToString(" ")}",
                e.cause?.let { " Caused by (${it.javaClass.canonicalName}): ${it.localizedMessage}" } ?: ""
            )

            is KubeConfigException, is ArgoGetWorkflowException -> listOf(
                "ERROR: ${e.message}",
                e.cause?.let { " Caused by (${it.javaClass.canonicalName}): ${it.localizedMessage}" } ?: ""
            )

            else -> null
        }?.let { errorLogs ->
            errorLogs.forEach { logger.error(it) }
            CommandHandlingResult.of("${errorLogs.joinToString("\n")}\n", 1)
        }
    }
}