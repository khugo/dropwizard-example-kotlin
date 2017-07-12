# Dropwizard Example in Kotlin
This is the example Dropwizard project from the [Dropwizard repo](https://github.com/dropwizard/dropwizard/tree/master/dropwizard-example) converted to Kotlin.

# Instructions

## Running with Gradle

Build the JAR:

`./gradlew build`

Running the migrations:

`java -jar build/libs/dropwizard-example-kotlin-1.2.0-SNAPSHOT-all.jar db migrate example.yml`

Running the application:

`java -jar build/libs/dropwizard-example-kotlin-1.2.0-SNAPSHOT-all.jar server example.yml`

## Running with Maven

Build the JAR:

`mvn clean package`

Running the migrations:

`java -jar target/dropwizard-example-1.2.0-SNAPSHOT.jar db migrate example.yml`

Running the application:

`java -jar target/dropwizard-example-1.2.0-SNAPSHOT.jar server example.yml`

See the original project README for more info:

https://github.com/dropwizard/dropwizard/tree/master/dropwizard-example

# Contributing

If you find any things that are incorrect or the original example project gets updated, please do send a pull request! There were some things that I wasn't able to get working (namely the mocks in `PeopleResourceTest` and `PersonResourceTest`)

