package com.soprabanking.dxp.argo.wrapper.client

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.soprabanking.dxp.argo.wrapper.exception.KubeConfigException
import com.soprabanking.dxp.argo.wrapper.exception.KubeWrapperException
import com.soprabanking.dxp.argo.wrapper.model.KubeConfig
import com.soprabanking.dxp.argo.wrapper.model.KubeConfig.Context
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ProcessBuilder.Redirect.INHERIT

@Component
class KubeClient {
    private val jsonMapper = ObjectMapper().also {
        it.registerModule(KotlinModule.Builder().build())
        it.enable(INDENT_OUTPUT)
        it.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val logger = LoggerFactory.getLogger(KubeClient::class.java)

    fun context(): Context {
        val output = mutableListOf<String>()
        run(mutableListOf("config", "view", "-o", "json", "--minify")) { output.add(it) }
        return runCatching {
            jsonMapper.readValue(output.joinToString(""), KubeConfig::class.java).contexts.first()
        }.getOrElse {
            throw KubeConfigException(it)
        }
    }

    fun run(
        args: List<String>,
        outputProcessor: ((String) -> Unit)? = null
    ) {
        var command = mutableListOf("kubectl")
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
                throw KubeWrapperException(command, output)
            }
        } catch (e: KubeWrapperException) {
            throw e
        } catch (e: Exception) {
            throw KubeWrapperException(command, output, e)
        } finally {
            output.forEach { logger.debug(it) }
        }
    }
}