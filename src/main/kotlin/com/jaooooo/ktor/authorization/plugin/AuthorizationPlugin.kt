package com.jaooooo.ktor.authorization.plugin

import com.jaooooo.ktor.authorization.plugin.model.*
import io.ktor.server.application.*
import io.ktor.server.auth.*



val RoleAuthorization = createRouteScopedPlugin(
    name = "AuthorizationPlugin",
    createConfiguration = ::AuthConfig
) {
    val requiredRoles = pluginConfig.roles
    val type = pluginConfig.type
    val getUserRoles = pluginConfig.getRoles

    on(AuthenticationChecked) {call ->
        val user = call.principal<RoleUser>() ?: throw AuthenticationException()
        val userRoles = getUserRoles(user)
        val denyReasons = mutableListOf<String>()

        when(type) {
            AuthType.ALL -> {
                val missing = requiredRoles - userRoles
                if (missing.isNotEmpty()) {
                    denyReasons += "Principal $user lacks required role(s) ${missing.joinToString(" and ")}"
                }
            }
            AuthType.ANY -> {
                if (userRoles.none { it in requiredRoles }) {
                    denyReasons += "Principal $user has none of the sufficient role(s) ${
                        requiredRoles.joinToString(
                            " or "
                        )
                    }"

                }
            }
            AuthType.NONE -> {
                if (userRoles.any{ it in requiredRoles}) {
                    denyReasons += "Principal $user has forbidden role(s) ${
                        (requiredRoles.intersect(userRoles)).joinToString(
                            " and "
                        )
                    }"

                }
            }
        }

        if (denyReasons.isNotEmpty()) {
            val message = denyReasons.joinToString(". ")
            throw AuthorizationException(message)
        }

    }

}