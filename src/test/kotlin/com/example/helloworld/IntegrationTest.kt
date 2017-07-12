package com.example.helloworld

import com.example.helloworld.api.Saying
import com.example.helloworld.core.Person
import io.dropwizard.Application
import io.dropwizard.testing.ConfigOverride
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test

import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Optional

import java.nio.charset.StandardCharsets.UTF_8
import org.assertj.core.api.Assertions.assertThat

class IntegrationTest {

    @Test
    fun testHelloWorld() {
        val name = Optional.of("Dr. IntegrationTest")
        val saying = RULE.client().target("http://localhost:" + RULE.localPort + "/hello-world")
                .queryParam("name", name.get())
                .request()
                .get(Saying::class.java)
        assertThat(saying.content).isEqualTo(RULE.configuration.buildTemplate().render(name))
    }

    @Test
    fun testPostPerson() {
        val person = Person(fullName = "Dr. IntegrationTest", jobTitle =  "Chief Wizard")
        val newPerson = RULE.client().target("http://localhost:" + RULE.localPort + "/people")
                .request()
                .post(Entity.entity<Any>(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person::class.java)
        assertThat(newPerson.id).isNotNull()
        assertThat(newPerson.fullName).isEqualTo(person.fullName)
        assertThat(newPerson.jobTitle).isEqualTo(person.jobTitle)
    }

    @Test
    fun testLogFileWritten() {
        // The log file is using a size and time based policy, which used to silently
        // fail (and not write to a log file). This test ensures not only that the
        // log file exists, but also contains the log line that jetty prints on startup
        val log = Paths.get("./logs/application.log")
        assertThat(log).exists()
        val actual = String(Files.readAllBytes(log), UTF_8)
        assertThat(actual).contains("0.0.0.0:" + RULE.localPort)
    }

    companion object {

        private val TMP_FILE = createTempFile()
        private val CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml")

        @ClassRule
        @JvmField
        val RULE = DropwizardAppRule(
                HelloWorldApplication::class.java, CONFIG_PATH,
                ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE))

        @BeforeClass
        @JvmStatic
        fun migrateDb() {
            RULE.getApplication<Application<HelloWorldConfiguration>>().run("db", "migrate", CONFIG_PATH)
        }

        private fun createTempFile(): String {
            return File.createTempFile("test-example", null).absolutePath
        }
    }
}
