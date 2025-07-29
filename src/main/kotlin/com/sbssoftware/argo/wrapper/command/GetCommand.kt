package com.sbssoftware.argo.wrapper.command

import com.sbssoftware.argo.wrapper.client.ArgoClient
import com.sbssoftware.argo.wrapper.command.ListCommand.Companion.ARGO_GROUP
import com.sbssoftware.argo.wrapper.context.ContextManager
import com.sbssoftware.argo.wrapper.context.model.ContextParameter
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command(group = ARGO_GROUP)
class GetCommand(val argoClient: ArgoClient, val contextManager: ContextManager) {
    @Command(description = "Get an Argo workflow")
    fun get(
        @Option(
            longNames = ["id"], label = "string",
            description = "Unique ID of the workflow to retry.\nIn interactive mode, defaults to the workflow of Argo wrapper context"
        ) id: String?,
        @Option(
            shortNames = ['n'], label = "string",
            description = "Namespace of the workflow to retry.\nIn interactive mode, defaults to the namespace of current Kube context"
        ) namespace: String?,
    ) {
        if (id != null) contextManager.set(ContextParameter.workflow, id)
        val workflowId = id ?: contextManager.context().workflowId!!
        argoClient.run(workflowId, namespace, listOf("get", workflowId))
    }
}