package com.soprabanking.dxp.argo.wrapper.exception

class ArgoWrapperException(
    workflowId: String?,
    namespace: String?,
    command: List<String>,
    logs: List<String>,
    cause: Throwable? = null,
) : WrapperException(
    "Failed to run Argo command ${forWorkflow(workflowId)} ${inNamespace(namespace)}.",
    command,
    logs,
    cause
) {
    companion object {
        private fun forWorkflow(workflowId: String?) = workflowId?.let { "for workflow $it" } ?: ""
        private fun inNamespace(namespace: String?) = namespace?.let { "in namespace $it" } ?: "in current namespace"
    }
}