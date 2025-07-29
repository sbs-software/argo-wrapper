package com.sbssoftware.argo.wrapper.exception

class KubeConfigException(cause: Throwable? = null) : RuntimeException("Failed to get Kube config", cause)