package com.example.helloworld.resources

import com.example.helloworld.core.User
import io.dropwizard.auth.Auth

import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

/**
 * [RolesAllowed], [PermitAll] are supported on the class level.
 *
 *
 * Method level annotations take precedence over the class level ones
 */

@Path("/protected")
@RolesAllowed("BASIC_GUY")
class ProtectedClassResource {

    @GET
    @PermitAll
    @Path("guest")
    fun showSecret(@Auth user: User): String {
        return String.format("Hey there, %s. You know the secret! %d", user.name, user.getId())
    }

    /* Access to this method is authorized by the class level annotation */
    @GET
    fun showBasicUserSecret(@Context context: SecurityContext): String {
        val user = context.userPrincipal as User
        return String.format("Hey there, %s. You seem to be a basic user. %d", user.name, user.getId())
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("admin")
    fun showAdminSecret(@Auth user: User): String {
        return String.format("Hey there, %s. It looks like you are an admin. %d", user.name, user.getId())
    }

}
