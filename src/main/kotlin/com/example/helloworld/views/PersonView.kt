package com.example.helloworld.views

import com.example.helloworld.core.Person
import io.dropwizard.views.View

class PersonView(template: PersonView.Template, val person: Person) : View(template.templateName) {

    enum class Template(val templateName: String) {
        FREEMARKER("freemarker/person.ftl"),
        MUSTACHE("mustache/person.mustache")
    }
}
