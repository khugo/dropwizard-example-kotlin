package com.example.helloworld.core

import java.lang.String.format
import java.util.*

class Template(private val content: String, private val defaultName: String) {
    fun render(name: Optional<String>): String {
        return format(content, name.orElse(defaultName))
    }
}
