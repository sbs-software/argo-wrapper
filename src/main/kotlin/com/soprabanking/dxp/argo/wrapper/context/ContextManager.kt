package com.soprabanking.dxp.argo.wrapper.context

import com.soprabanking.dxp.argo.wrapper.client.KubeClient
import com.soprabanking.dxp.argo.wrapper.context.model.ContextParameter
import org.springframework.stereotype.Component

@Component
class ContextManager(private val kubeClient: KubeClient) {

    private var context = ArgoWrapperContext()

    fun context() = context

    fun set(parameter: ContextParameter, value: String) {
        when (parameter) {
            ContextParameter.workflow -> context = context.copy(workflowId = value)
            ContextParameter.namespace -> {
                kubeClient.run(listOf("config", "set-context", "--current", "--namespace=$value")) {}
                clear()
            }
        }
    }

    fun clear() {
        kubeClient.context().let {
            context = ArgoWrapperContext(it.context.cluster, it.context.namespace)
        }
    }

    data class ArgoWrapperContext(
        val cluster: String? = null,
        val namespace: String? = null,
        val workflowId: String? = null,
    )
}