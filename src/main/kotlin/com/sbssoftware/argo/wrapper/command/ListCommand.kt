package com.sbssoftware.argo.wrapper.command

import com.sbssoftware.argo.wrapper.client.ArgoClient
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command(group = ListCommand.Companion.ARGO_GROUP)
class ListCommand(val argoClient: ArgoClient) {
    companion object {
        const val ARGO_GROUP = "Argo Wrapper Commands"
    }

    @Command(description = "List Argo workflows")
    fun list(
        @Option(
            shortNames = ['n'], label = "string",
            description = "Namespace of the workflow to retry.\nDefaults to the namespace of current Kube context"
        ) namespace: String?,
    ) {
        argoClient.run(null, namespace, listOf("list"))
    }
}