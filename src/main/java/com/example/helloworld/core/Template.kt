package com.example.helloworld.core

import java.util.Optional

import java.lang.String.format

class Template(private val content: String, private val defaultName: String) {
    fun render(name: Optional<String>): String {
        return format(content, name.orElse(defaultName))
    }
}
