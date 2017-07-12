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
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.HttpHeaders

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown

class ProtectedResourceTest {

    @Test
    fun testProtectedEndpoint() {
        val secret = RULE.target("/protected").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, good-guy. You know the secret!")
    }

    @Test
    fun testProtectedEndpointNoCredentials401() {
        try {
            RULE.target("/protected").request()
                    .get(String::class.java)
            failBecauseExceptionWasNotThrown(NotAuthorizedException::class.java)
        } catch (e: NotAuthorizedException) {
            assertThat(e.response.status).isEqualTo(401)
            assertThat(e.response.headers[HttpHeaders.WWW_AUTHENTICATE])
                    .containsOnly("Basic realm=\"SUPER SECRET STUFF\"")
        }

    }

    @Test
    fun testProtectedEndpointBadCredentials401() {
        try {
            RULE.target("/protected").request()
                    .header(HttpHeaders.AUTHORIZATION, "Basic c25lYWt5LWJhc3RhcmQ6YXNkZg==")
                    .get(String::class.java)
            failBecauseExceptionWasNotThrown(NotAuthorizedException::class.java)
        } catch (e: NotAuthorizedException) {
            assertThat(e.response.status).isEqualTo(401)
            assertThat(e.response.headers[HttpHeaders.WWW_AUTHENTICATE])
                    .containsOnly("Basic realm=\"SUPER SECRET STUFF\"")
        }

    }

    @Test
    fun testProtectedAdminEndpoint() {
        val secret = RULE.target("/protected/admin").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic Y2hpZWYtd2l6YXJkOnNlY3JldA==")
                .get(String::class.java)
        assertThat(secret).startsWith("Hey there, chief-wizard. It looks like you are an admin.")
    }

    @Test
    fun testProtectedAdminEndpointPrincipalIsNotAuthorized403() {
        try {
            RULE.target("/protected/admin").request()
                    .header(HttpHeaders.AUTHORIZATION, "Basic Z29vZC1ndXk6c2VjcmV0")
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
                .addProvider(ProtectedResource::class.java)
                .build()
    }
}
