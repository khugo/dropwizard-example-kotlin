package com.example.helloworld

import com.example.helloworld.core.Template
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.Valid
import javax.validation.constraints.NotNull

class HelloWorldConfiguration : Configuration() {
    @NotEmpty
    @JsonProperty
    private lateinit var template: String

    @NotEmpty
    @JsonProperty
    private val defaultName = "Stranger"

    @Valid
    @NotNull
    @JsonProperty("database")
    val dataSourceFactory = DataSourceFactory()

    @NotNull
    @JsonProperty("viewRendererConfiguration")
    val viewRendererConfiguration: Map<String, Map<String, String>> = mutableMapOf()

    fun buildTemplate() = Template(template, defaultName)
}
