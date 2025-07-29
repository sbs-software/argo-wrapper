package com.sbssoftware.argo.wrapper.exception

class KubeWrapperException(
    command: List<String>,
    logs: List<String>,
    cause: Throwable? = null,
) : WrapperException("Failed to run Kubectl command.", command, logs, cause)