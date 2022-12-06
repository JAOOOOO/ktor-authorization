package plugin.model

class AuthorizationException(override val message: String? = null) : Throwable()

class AuthenticationException(override val message: String? = null) : Throwable()
