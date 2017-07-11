package com.example.helloworld.resources

import com.example.helloworld.core.Person
import com.example.helloworld.db.PersonDAO
import com.example.helloworld.views.PersonView
import io.dropwizard.hibernate.UnitOfWork
import io.dropwizard.jersey.params.LongParam
import org.slf4j.LoggerFactory

import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/people/{personId}")
@Produces(MediaType.APPLICATION_JSON)
class PersonResource(private val peopleDAO: PersonDAO) {

    @GET
    @UnitOfWork
    fun getPerson(@PathParam("personId") personId: LongParam): Person {
        return findSafely(personId.get())
    }

    @GET
    @Path("/view_freemarker")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    fun getPersonViewFreemarker(@PathParam("personId") personId: LongParam): PersonView {
        return PersonView(PersonView.Template.FREEMARKER, findSafely(personId.get()))
    }

    @GET
    @Path("/view_mustache")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    fun getPersonViewMustache(@PathParam("personId") personId: LongParam): PersonView {
        return PersonView(PersonView.Template.MUSTACHE, findSafely(personId.get()))
    }

    private fun findSafely(personId: Long): Person {
        return peopleDAO.findById(personId).orElseThrow { NotFoundException("No such user.") }
    }
}
