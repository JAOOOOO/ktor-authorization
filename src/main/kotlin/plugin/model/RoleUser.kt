@file:Suppress("unused")

package plugin.model

import io.ktor.server.auth.*

abstract class RoleUser : Principal {
    abstract val roles : Set<Role>
}

