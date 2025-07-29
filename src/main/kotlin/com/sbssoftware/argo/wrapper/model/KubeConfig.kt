package com.sbssoftware.argo.wrapper.model

data class KubeConfig(val contexts: List<Context> = emptyList()) {
    data class Context(val name: String, val context: ContextDetails)
    data class ContextDetails(val cluster: String, val namespace: String)
}