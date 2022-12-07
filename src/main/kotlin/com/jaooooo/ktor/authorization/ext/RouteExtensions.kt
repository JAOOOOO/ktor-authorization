package com.jaooooo.ktor.authorization.ext

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.jaooooo.ktor.authorization.plugin.RoleAuthorization
import com.jaooooo.ktor.authorization.plugin.model.AuthType
import com.jaooooo.ktor.authorization.plugin.model.Role


///Applies logical AND between roles
fun Route.withAllRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(
        requiredRoles = roles.toSet(),
        authType = AuthType.ALL,
        build = build,
    )
///Applies logical OR between roles
fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(
        requiredRoles = roles.toSet(),
        authType = AuthType.ANY,
        build = build,
    )

///Applies logical NOT for provided roles
fun Route.withoutRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(
        requiredRoles = roles.toSet(),
        authType = AuthType.NONE,
        build = build,
    )

private fun Route.authorizedRoute(
    requiredRoles: Set<Role>,
    authType: AuthType,
    build: Route.() -> Unit
): Route {

  val description = requiredRoles.joinToString(",")
  val authorizedRoute = createChild(AuthorizedRouteSelector(description))

  authorizedRoute.install(RoleAuthorization) {
    roles = requiredRoles
    type = authType
  }
  authorizedRoute.build()
  return authorizedRoute
}
