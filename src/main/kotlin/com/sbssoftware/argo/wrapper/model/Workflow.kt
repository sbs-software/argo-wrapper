package com.sbssoftware.argo.wrapper.model

data class Workflow(val spec: Spec) {
    data class Spec(val arguments: Arguments = Arguments())
    data class Arguments(val parameters: List<Parameter> = emptyList())
    data class Parameter(val name: String, val value: String)
}
