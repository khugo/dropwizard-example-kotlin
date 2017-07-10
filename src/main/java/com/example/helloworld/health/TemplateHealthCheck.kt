package com.example.helloworld.health

import com.codahale.metrics.health.HealthCheck
import com.example.helloworld.core.Template

import java.util.Optional

class TemplateHealthCheck(private val template: Template) : HealthCheck() {

    @Throws(Exception::class)
    override fun check(): HealthCheck.Result {
        template.render(Optional.of("woo"))
        template.render(Optional.empty())
        return HealthCheck.Result.healthy()
    }
}
