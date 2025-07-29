package com.sbssoftware.argo.wrapper.exception

open class WrapperException(
    message: String,
    val command: List<String>,
    val logs: List<String>,
    cause: Throwable? = null,
) : RuntimeException(message, cause)