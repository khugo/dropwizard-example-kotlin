package com.example.helloworld.db

import com.example.helloworld.core.Person
import io.dropwizard.testing.junit.DAOTestRule
import org.hibernate.exception.ConstraintViolationException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Optional

import org.assertj.core.api.Assertions.assertThat

class PersonDAOTest {

    @Rule
    @JvmField
    var daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Person::class.java)
            .build()

    private var personDAO: PersonDAO? = null

    @Before
    fun setUp() {
        personDAO = PersonDAO(daoTestRule.sessionFactory)
    }

    @Test
    fun createPerson() {
        val jeff = daoTestRule.inTransaction<Person> { personDAO!!.create(Person(fullName = "Jeff", jobTitle = "The plumber")) }
        assertThat(jeff.id).isGreaterThan(0)
        assertThat(jeff.fullName).isEqualTo("Jeff")
        assertThat(jeff.jobTitle).isEqualTo("The plumber")
        assertThat(personDAO!!.findById(jeff.id)).isEqualTo(Optional.of(jeff))
    }

    @Test
    fun findAll() {
        daoTestRule.inTransaction {
            personDAO!!.create(Person(fullName = "Jeff", jobTitle = "The plumber"))
            personDAO!!.create(Person(fullName = "Jim", jobTitle = "The cook"))
            personDAO!!.create(Person(fullName = "Randy", jobTitle = "The watchman"))
        }

        val persons = personDAO!!.findAll()
        assertThat(persons).extracting("fullName").containsOnly("Jeff", "Jim", "Randy")
        assertThat(persons).extracting("jobTitle").containsOnly("The plumber", "The cook", "The watchman")
    }
}
