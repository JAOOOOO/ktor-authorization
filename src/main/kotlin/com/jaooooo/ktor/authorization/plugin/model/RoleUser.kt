@file:Suppress("unused")

package com.jaooooo.ktor.authorization.plugin.model

import io.ktor.server.auth.*

typealias Role = String

abstract class RoleUser : Principal {
    abstract val roles : Set<Role>
}

