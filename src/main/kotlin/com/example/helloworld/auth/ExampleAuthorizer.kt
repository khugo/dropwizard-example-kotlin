package com.example.helloworld.auth

import com.example.helloworld.core.User
import io.dropwizard.auth.Authorizer

class ExampleAuthorizer : Authorizer<User> {
    override fun authorize(user: User, role: String): Boolean {
        return user.roles != null && user.roles.contains(role)
    }
}
