package com.soprabanking.dxp.argo.wrapper.command

import com.soprabanking.dxp.argo.wrapper.client.ArgoClient
import com.soprabanking.dxp.argo.wrapper.command.ListCommand.Companion.ARGO_GROUP
import org.springframework.shell.command.CommandRegistration.OptionArity.ZERO_OR_MORE
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command(group = ARGO_GROUP)
class RetryCommand(val argoClient: ArgoClient) {

    @Command(description = "Retry an Argo workflow")
    fun retry(
        @Option(
            longNames = ["id"], label = "string",
            description = "Unique ID of the workflow to retry", required = true
        ) workflowId: String,
        @Option(
            shortNames = ['n'], label = "string",
            description = "Namespace of the workflow to retry"
        ) namespace: String?,
        @Option(
            description = "Watch the workflow until it completes",
            defaultValue = "false"
        ) watch: Boolean,
        @Option(
            shortNames = ['t'], label = "string",
            description = "Name of the task to retry in a DAG workflow"
        ) target: String?,
        @Option(
            longNames = ["skip-dependencies", "skip"], shortNames = ['s'],
            description = "Skip dependent tasks when retrying a task in a DAG workflow",
            defaultValue = "false"
        ) skipDependencies: Boolean,
        @Option(
            longNames = ["parameter"], shortNames = ['p'], label = "name=value", arity = ZERO_OR_MORE,
            description = "Input parameters added to the retried workflow"
        ) parameters: List<String>?,
    ) {
        var command = mutableListOf("retry", workflowId)
        if (target == null) {
            command += listOf("-p", "skip_dependencies=false", "-p", "target=")
        } else {
            command += listOf("--restart-successful", "--node-field-selector", "displayName=$target")
            command += if (skipDependencies) {
                listOf("-p", "skip_dependencies=true", "-p", "target=$target")
            } else {
                listOf("-p", "skip_dependencies=false", "-p", "target=")
            }
        }
        if (watch) command += listOf("--watch")
        command += listOf("-p", "attempt=${nextAttemptFor(workflowId, namespace)}")
        parameters?.forEach { command += listOf("-p", it) }
        argoClient.run(workflowId, namespace, command)
    }

    private fun nextAttemptFor(workflowId: String, namespace: String?): Int {
        val workflow = argoClient.get(workflowId, namespace)
        return (workflow.spec.arguments.parameters.firstOrNull { it.name == "attempt" }?.value?.toInt() ?: 0) + 1
    }
}