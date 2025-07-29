package com.sbssoftware.argo.wrapper.command

import com.sbssoftware.argo.wrapper.client.KubeClient
import com.sbssoftware.argo.wrapper.context.ContextManager
import com.sbssoftware.argo.wrapper.context.model.ContextParameter
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option
import org.springframework.shell.context.InteractionMode.INTERACTIVE

@Command(command = ["context"], group = ContextCommand.Companion.CONTEXT_GROUP, interactionMode = [INTERACTIVE])
class ContextCommand(val contextManager: ContextManager, val kubeClient: KubeClient) {
    companion object {
        const val CONTEXT_GROUP = "Context commands"
    }

    @Command(command = ["kube list"], description = "List kubernetes contexts")
    fun kubeList() {
        kubeClient.run(listOf("config", "get-contexts"))
    }

    @Command(command = ["kube set"], description = "Set kubernetes context")
    fun kubeSet(
        @Option(
            label = "context", required = true,
            description = "Name of the kubernetes context"
        ) context: String
    ) {
        kubeClient.run(listOf("config", "use-context", context)) {}
        contextManager.clear()
    }

    @Command(command = ["set"], description = "Set parameter in Argo wrapper context")
    fun set(
        @Option(
            label = "namespace|workflow", required = true,
            description = "Name of the parameter to store in context"
        ) parameter: ContextParameter,
        @Option(
            label = "string", required = true,
            description = "Value of the parameter to store in context"
        ) value: String,
    ) {
        contextManager.set(parameter, value)
    }

    @Command(
        command = ["clear"],
        description = "Reset or initialize all parameters of Argo wrapper context",
        alias = ["init"]
    )
    fun clear() {
        contextManager.clear()
    }
}