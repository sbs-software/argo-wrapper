package com.soprabanking.dxp.argo.wrapper.exception

class KubeConfigException(cause: Throwable? = null) : RuntimeException("Failed to get Kube config", cause)