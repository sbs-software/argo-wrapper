package com.soprabanking.dxp.argo.wrapper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.shell.command.annotation.CommandScan

@SpringBootApplication
@CommandScan(basePackages = ["com.soprabanking.dxp.argo.wrapper.command"])
class ArgoRetryApplication

fun main(args: Array<String>) {
    runApplication<ArgoRetryApplication>(*args)
}
