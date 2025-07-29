package com.sbssoftware.argo.wrapper.context

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle.BOLD
import org.jline.utils.AttributedStyle.YELLOW
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class ArgoPromptProvider(private val contextManager: ContextManager) : PromptProvider {

    override fun getPrompt() = contextManager.context().let { context ->
        AttributedString(
            """
            [cluster: ${context.cluster ?: "-"}] [namespace: ${context.namespace ?: "-"}] [workflow: ${context.workflowId ?: "-"}]
            argow:> 
            """.trimIndent(), BOLD.foreground(YELLOW)
        )
    }
}