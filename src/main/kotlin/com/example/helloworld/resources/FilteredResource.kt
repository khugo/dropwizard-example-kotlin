package com.example.helloworld.resources

import com.example.helloworld.filter.DateRequired
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/filtered")
class FilteredResource {
    @GET
    @DateRequired
    @Path("hello")
    fun sayHello(): String {
        return "hello"
    }
}
