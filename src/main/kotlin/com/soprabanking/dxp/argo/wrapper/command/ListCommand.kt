package com.soprabanking.dxp.argo.wrapper.command

import com.soprabanking.dxp.argo.wrapper.client.ArgoClient
import com.soprabanking.dxp.argo.wrapper.command.ListCommand.Companion.ARGO_GROUP
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command(group = ARGO_GROUP)
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