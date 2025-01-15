package com.soprabanking.dxp.argo.wrapper.command

import com.soprabanking.dxp.argo.wrapper.client.ArgoClient
import com.soprabanking.dxp.argo.wrapper.command.ListCommand.Companion.ARGO_GROUP
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command(group = ARGO_GROUP)
class GetCommand(val argoClient: ArgoClient) {
    @Command(description = "Get an Argo workflow")
    fun get(
        @Option(
            longNames = ["id"], label = "string",
            description = "Unique ID of the workflow to retry", required = true
        ) workflowId: String,
        @Option(
            shortNames = ['n'], label = "string",
            description = "Namespace of the workflow to retry"
        ) namespace: String?,
    ) {
        argoClient.run(workflowId, namespace, listOf("get", workflowId))
    }
}