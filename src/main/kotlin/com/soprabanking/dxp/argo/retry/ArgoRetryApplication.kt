package com.soprabanking.dxp.argo.retry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArgoRetryApplication

fun main(args: Array<String>) {
	runApplication<ArgoRetryApplication>(*args)
}
