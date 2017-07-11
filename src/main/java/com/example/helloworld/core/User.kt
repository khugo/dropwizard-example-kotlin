package com.example.helloworld.core

import java.security.Principal

class User(private val name: String, val roles: Set<String>? = null) : Principal {
    override fun getName(): String {
        return name
    }

    fun getId(): Int {
        return (Math.random() * 100).toInt()
    }
}
