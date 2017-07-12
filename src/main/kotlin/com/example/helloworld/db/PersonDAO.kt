package com.example.helloworld.db

import com.example.helloworld.core.Person
import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.hibernate.query.Query
import java.util.*

open class PersonDAO(factory: SessionFactory) : AbstractDAO<Person>(factory) {

    fun findById(id: Long): Optional<Person> {
        return Optional.ofNullable(get(id))
    }

    fun create(person: Person): Person {
        return persist(person)
    }

    fun findAll(): List<Person> {
        return list(namedQuery("com.example.helloworld.core.Person.findAll") as? Query<Person>)
    }
}
