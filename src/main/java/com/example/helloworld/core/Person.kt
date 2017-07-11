package com.example.helloworld.core

import javax.persistence.*

@Entity
@Table(name = "people")
@NamedQueries(NamedQuery(name = "com.example.helloworld.core.Person.findAll", query = "SELECT p FROM Person p"))
data class Person(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "fullName", nullable = false)
        val fullName: String,
        @Column(name = "jobTitle", nullable = false)
        val jobTitle: String
)
