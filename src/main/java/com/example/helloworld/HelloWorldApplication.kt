package com.example.helloworld

import com.example.helloworld.auth.ExampleAuthenticator
import com.example.helloworld.auth.ExampleAuthorizer
import com.example.helloworld.cli.RenderCommand
import com.example.helloworld.core.Person
import com.example.helloworld.core.User
import com.example.helloworld.db.PersonDAO
import com.example.helloworld.filter.DateRequiredFeature
import com.example.helloworld.health.TemplateHealthCheck
import com.example.helloworld.resources.*
import com.example.helloworld.tasks.EchoTask
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.auth.AuthDynamicFeature
import io.dropwizard.auth.AuthValueFactoryProvider
import io.dropwizard.auth.basic.BasicCredentialAuthFilter
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.hibernate.HibernateBundle
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature

class HelloWorldApplication : Application<HelloWorldConfiguration>() {

    private val hibernateBundle = object : HibernateBundle<HelloWorldConfiguration>(Person::class.java) {
        override fun getDataSourceFactory(configuration: HelloWorldConfiguration): DataSourceFactory {
            return configuration.dataSourceFactory
        }
    }

    override fun getName(): String {
        return "hello-world"
    }

    override fun initialize(bootstrap: Bootstrap<HelloWorldConfiguration>) {
        // Enable variable substitution with environment variables
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(
                bootstrap.configurationSourceProvider,
                EnvironmentVariableSubstitutor(false)
        )

        bootstrap.addCommand(RenderCommand())
        bootstrap.addBundle(AssetsBundle())
        bootstrap.addBundle(object : MigrationsBundle<HelloWorldConfiguration>() {
            override fun getDataSourceFactory(configuration: HelloWorldConfiguration): DataSourceFactory {
                return configuration.dataSourceFactory
            }
        })
        bootstrap.addBundle(hibernateBundle)
        bootstrap.addBundle(object : ViewBundle<HelloWorldConfiguration>() {
            override fun getViewConfiguration(configuration: HelloWorldConfiguration): Map<String, Map<String, String>> {
                return configuration.viewRendererConfiguration
            }
        })
    }

    override fun run(configuration: HelloWorldConfiguration, environment: Environment) {
        val dao = PersonDAO(hibernateBundle.sessionFactory)
        val template = configuration.buildTemplate()

        environment.healthChecks().register("template", TemplateHealthCheck(template))
        environment.admin().addTask(EchoTask())
        environment.jersey().register(DateRequiredFeature::class.java)
        environment.jersey().register(AuthDynamicFeature(BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(ExampleAuthenticator())
                .setAuthorizer(ExampleAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()))
        environment.jersey().register(AuthValueFactoryProvider.Binder(User::class.java))
        environment.jersey().register(RolesAllowedDynamicFeature::class.java)
        environment.jersey().register(HelloWorldResource(template))
        environment.jersey().register(ViewResource())
        environment.jersey().register(ProtectedResource())
        environment.jersey().register(PeopleResource(dao))
        environment.jersey().register(PersonResource(dao))
        environment.jersey().register(FilteredResource())
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic fun main(args: Array<String>) {
            HelloWorldApplication().run(*args)
        }
    }
}
