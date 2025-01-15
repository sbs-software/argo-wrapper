package com.soprabanking.dxp.argo.wrapper.client

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.soprabanking.dxp.argo.wrapper.exception.ArgoClientException
import com.soprabanking.dxp.argo.wrapper.exception.ArgoGetWorkflowException
import com.soprabanking.dxp.argo.wrapper.model.Workflow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ProcessBuilder.Redirect.INHERIT

@Component
class ArgoClient {
    private val jsonMapper = ObjectMapper().also {
        it.registerModule(KotlinModule.Builder().build())
        it.enable(INDENT_OUTPUT)
        it.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val logger = LoggerFactory.getLogger(ArgoClient::class.java)

    fun get(workflowId: String, namespace: String?): Workflow {
        val output = mutableListOf<String>()
        run(workflowId, namespace, mutableListOf("get", workflowId, "-o", "json")) { output.add(it) }
        return runCatching {
            jsonMapper.readValue(output.joinToString(""), Workflow::class.java)
        }.getOrElse {
            throw ArgoGetWorkflowException(workflowId, namespace, it)
        }
    }

    fun run(
        workflowId: String?,
        namespace: String?,
        args: List<String>,
        outputProcessor: ((String) -> Unit)? = null
    ) {
        var command = mutableListOf("argo")
        if (namespace != null) {
            command += listOf("-n", namespace)
        }
        command += args
        val output = mutableListOf<String>()
        try {
            logger.info("Executing command: ${command.joinToString(" ")}")
            val processBuilder = ProcessBuilder(command)
            if (outputProcessor != null) {
                processBuilder.redirectErrorStream(true)
            } else {
                processBuilder.redirectError(INHERIT)
                processBuilder.redirectOutput(INHERIT)
            }
            val process = processBuilder.start()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    output.add(line)
                    outputProcessor?.invoke(line)
                    line = reader.readLine()
                }
            }
            if (process.waitFor() != 0) {
                throw ArgoClientException(workflowId, namespace, command, output)
            }
        } catch (e: ArgoClientException) {
            throw e
        } catch (e: Exception) {
            throw ArgoClientException(workflowId, namespace, command, output, e)
        } finally {
            output.forEach { logger.debug(it) }
        }
    }
}