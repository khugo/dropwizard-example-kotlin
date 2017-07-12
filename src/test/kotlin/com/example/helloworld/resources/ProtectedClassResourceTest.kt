package com.example.helloworld.resources

import com.example.helloworld.auth.ExampleAuthenticator
import com.example.helloworld.auth.ExampleAuthorizer
import com.example.helloworld.core.User
import io.dropwizard.auth.AuthDynamicFeature
import io.dropwizard.auth.AuthValueFactoryProvider
import io.dropwizard.auth.basic.BasicCredentialAuthFilter
import io.dropwizard.testing.junit.ResourceTestRule
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.junit.ClassRule
import org.junit.Test

import javax.ws.rs.ForbiddenException
import javax.ws.rs.core.HttpHeaders

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown

class ProtectedClassResourceTest {

    @Test
    fun testProtectedAdminEndpoint() {
        val secret = RULE.target("/protected/admin").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Y2hpZWYtd2l6YXJkOnNlY3JldA==")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, chief-wizard. It looks like you are an admin.")
    }

    @Test
    fun testProtectedBasicUserEndpoint() {
        val secret = RULE.target("/protected").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, good-guy. You seem to be a basic user.")
    }

    @Test
    fun testProtectedBasicUserEndpointAsAdmin() {
        val secret = RULE.target("/protected").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Y2hpZWYtd2l6YXJkOnNlY3JldA==")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, chief-wizard. You seem to be a basic user.")
    }

    @Test
    fun testProtectedGuestEndpoint() {
        val secret = RULE.target("/protected/guest").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Z3Vlc3Q6c2VjcmV0")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, guest. You know the secret!")
    }

    @Test
    fun testProtectedBasicUserEndpointPrincipalIsNotAuthorized403() {
        try {
            RULE.target("/protected").request()
                    .header(HttpHeaders.AUTHORIZATION, "Basic Z3Vlc3Q6c2VjcmV0")
                    .get(String::class.java)
            failBecauseExceptionWasNotThrown(ForbiddenException::class.java)
        } catch (e: ForbiddenException) {
            assertThat(e.response.status).isEqualTo(403)
        }

    }

    companion object {

        private val BASIC_AUTH_HANDLER = BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(ExampleAuthenticator())
                .setAuthorizer(ExampleAuthorizer())
                .setPrefix("Basic")
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()

        @ClassRule
        @JvmField
        val RULE = ResourceTestRule.builder()
                .addProvider(RolesAllowedDynamicFeature::class.java)
                .addProvider(AuthDynamicFeature(BASIC_AUTH_HANDLER))
                .addProvider(AuthValueFactoryProvider.Binder(User::class.java))
                .setTestContainerFactory(GrizzlyWebTestContainerFactory())
                .addProvider(ProtectedClassResource::class.java)
                .build()
    }

}
