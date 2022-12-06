package plugin.model

typealias Role = String
class AuthConfig {
    ///Required Roles to satisfy [authType] conditions
    var roles : Set<Role> = emptySet()
    ///How to get the users' roles
    var getRoles: (RoleUser) -> Set<Role> = {
        it.roles
    }
    ///Authorization logic
    var type: AuthType = AuthType.ALL
}