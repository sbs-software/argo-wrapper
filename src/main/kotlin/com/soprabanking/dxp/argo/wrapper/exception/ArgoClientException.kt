package com.soprabanking.dxp.argo.wrapper.exception

class ArgoClientException(
    workflowId: String?,
    namespace: String?,
    val command: List<String>,
    val logs: List<String>,
    cause: Throwable? = null,
) : RuntimeException("Failed to run Argo command ${forWorkflow(workflowId)} ${inNamespace(namespace)}.", cause) {
    companion object {
        private fun forWorkflow(workflowId: String?) = workflowId?.let { "for workflow $it" } ?: ""
        private fun inNamespace(namespace: String?) = namespace?.let { "in namespace $it" } ?: "in current namespace"
    }
}