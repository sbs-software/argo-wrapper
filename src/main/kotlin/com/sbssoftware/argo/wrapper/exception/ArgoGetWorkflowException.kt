package com.sbssoftware.argo.wrapper.exception

class ArgoGetWorkflowException(
    workflowId: String,
    namespace: String?,
    cause: Throwable? = null,
) : RuntimeException("Failed to get Argo workflow $workflowId ${inNamespace(namespace)}", cause) {
    companion object {
        private fun inNamespace(namespace: String?) = namespace?.let { "in namespace $it" } ?: "in current namespace"
    }
}