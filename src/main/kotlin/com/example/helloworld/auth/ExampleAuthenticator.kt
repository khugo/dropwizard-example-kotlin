package com.example.helloworld.auth

import com.example.helloworld.core.User
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import io.dropwizard.auth.AuthenticationException
import io.dropwizard.auth.Authenticator
import io.dropwizard.auth.basic.BasicCredentials
import java.util.*

class ExampleAuthenticator : Authenticator<BasicCredentials, User> {

    @Throws(AuthenticationException::class)
    override fun authenticate(credentials: BasicCredentials): Optional<User> {
        return if (VALID_USERS.containsKey(credentials.username) && "secret" == credentials.password) {
            Optional.of(User(credentials.username, VALID_USERS[credentials.username]))
        } else Optional.empty()
    }

    companion object {
        /**
         * Valid users with mapping user -> roles
         */
        private val VALID_USERS = ImmutableMap.of<String, Set<String>>(
                "guest", ImmutableSet.of(),
                "good-guy", ImmutableSet.of("BASIC_GUY"),
                "chief-wizard", ImmutableSet.of("ADMIN", "BASIC_GUY")
        )
    }
}
