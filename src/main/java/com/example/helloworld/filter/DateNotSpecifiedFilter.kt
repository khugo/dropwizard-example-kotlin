package com.example.helloworld.filter

import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider
import java.io.IOException

@Provider
class DateNotSpecifiedFilter : ContainerRequestFilter {
    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext) {
        requestContext.getHeaderString(HttpHeaders.DATE) ?: throw WebApplicationException(IllegalArgumentException("Date Header was not specified"),
                Response.Status.BAD_REQUEST)
    }
}
