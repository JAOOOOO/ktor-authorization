package ext

import io.ktor.server.application.*
import io.ktor.server.routing.*
import plugin.RoleAuthorization
import plugin.model.AuthType
import plugin.model.Role

fun Route.withRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(
        requiredRoles = roles.toSet(),
        authType = AuthType.ALL,
        build = build,
    )

fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(
        requiredRoles = roles.toSet(),
        authType = AuthType.ANY,
        build = build,
    )

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
