package com.example.helloworld.resources

import com.codahale.metrics.annotation.Timed
import com.example.helloworld.api.Saying
import com.example.helloworld.core.Template
import io.dropwizard.jersey.caching.CacheControl
import io.dropwizard.jersey.params.DateTimeParam
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldResource(private val template: Template) {
    private val LOGGER = LoggerFactory.getLogger(HelloWorldResource::class.java)
    private val counter = AtomicLong()

    @GET
    @Timed(name = "get-requests")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    fun sayHello(@QueryParam("name") name: Optional<String>): Saying {
        return Saying(counter.incrementAndGet(), template.render(name))
    }

    @POST
    fun receiveHello(@Valid saying: Saying) {
        LOGGER.info("Received a saying: {}", saying)
    }

    @GET
    @Path("/date")
    @Produces(MediaType.TEXT_PLAIN)
    fun receiveDate(@QueryParam("date") dateTimeParam: Optional<DateTimeParam>): String? {
        if (dateTimeParam.isPresent) {
            val actualDateTimeParam = dateTimeParam.get()
            LOGGER.info("Received a date: {}", actualDateTimeParam)
            return actualDateTimeParam.get().toString()
        } else {
            LOGGER.warn("No received date")
            return null
        }
    }
}
