package com.example.helloworld.resources

import com.example.helloworld.core.Person
import com.example.helloworld.db.PersonDAO
import io.dropwizard.hibernate.UnitOfWork
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
class PeopleResource(private val peopleDAO: PersonDAO) {
    @POST
    @UnitOfWork
    fun createPerson(person: Person): Person {
        return peopleDAO.create(person)
    }

    @GET
    @UnitOfWork
    fun listPeople(): List<Person> {
        return peopleDAO.findAll()
    }
}
