import com.jaooooo.ktor.authorization.ext.withAnyRole
import com.jaooooo.ktor.authorization.ext.withAllRoles
import com.jaooooo.ktor.authorization.ext.withoutRoles
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import org.junit.Assert.*
import org.junit.Test
import com.jaooooo.ktor.authorization.plugin.model.AuthenticationException
import com.jaooooo.ktor.authorization.plugin.model.AuthorizationException
import com.jaooooo.ktor.authorization.plugin.model.Role
import com.jaooooo.ktor.authorization.plugin.model.RoleUser
import kotlin.test.assertFailsWith

class RoleUserImp (override val roles: Set<Role> = setOf("ABC")) : RoleUser()
class RoutingTest {

    @Test
    fun `can authorize with ALL`() = testApplication {

        install(Sessions) {
            cookie<RoleUserImp>("user_session")
        }

        install(Authentication) {
            session<RoleUserImp> {
                challenge {

                }
                validate {
                    it
                }
            }
        }

        val testHttpClient = createClient {
            install(HttpCookies)
        }




        routing {
            get("/auth") {
                call.sessions.set<RoleUserImp>(RoleUserImp())
            }
            authenticate {
                withAllRoles("ABC") {
                    get("/any-test") {
                        call.respondText(status = HttpStatusCode.OK, text = "You're good")
                    }
                }
            }

        }

        testHttpClient.get("/auth")
       val anyResponse = testHttpClient.get("/any-test")
        assertEquals(HttpStatusCode.OK, anyResponse.status)
    }

    @Test
    fun `can authorize with ANY`() = testApplication {

        install(Sessions) {
            cookie<RoleUserImp>("user_session")
        }

        install(Authentication) {
            session<RoleUserImp> {
                challenge {

                }
                validate {
                    it
                }
            }
        }

        val testHttpClient = createClient {
            install(HttpCookies)
        }




        routing {
            get("/auth") {
                call.sessions.set(RoleUserImp())
            }
            authenticate {
                withAnyRole("ABC", "LOL", "Super") {
                    get("/any-test") {
                        call.respondText(status = HttpStatusCode.OK, text = "You're good")
                    }
                }
            }

        }

        testHttpClient.get("/auth")
        val anyResponse = testHttpClient.get("/any-test")
        assertEquals(HttpStatusCode.OK, anyResponse.status)
    }

    @Test
    fun `throws AuthorizationException if user has a Role marked with None`() = testApplication {

        install(Sessions) {
            cookie<RoleUserImp>("user_session")
        }

        install(Authentication) {
            session<RoleUserImp> {
                challenge {

                }
                validate {
                    it
                }
            }
        }

        val testHttpClient = createClient {
            install(HttpCookies)
        }




        routing {
            get("/auth") {
                call.sessions.set<RoleUserImp>(RoleUserImp())
            }
            authenticate {
                withoutRoles("ABC") {
                    get("/any-test") {
                        call.respondText(status = HttpStatusCode.OK, text = "You're good")
                    }
                }
            }

        }

        testHttpClient.get("/auth")
        assertFailsWith<AuthorizationException> {
            testHttpClient.get("/any-test")
        }
    }
    @Test
    fun `throws AuthenticationException if no user is set`() = testApplication {
        val testHttpClient = createClient {
            install(HttpCookies)
        }

        install(Sessions) {
            cookie<RoleUserImp>("user_session")
        }
        install(Authentication) {
            session<RoleUserImp> {
                challenge {

                }
                validate {
                    it
                }
            }
        }


        routing {
            authenticate {
                withAllRoles("ABC") {
                    get("/any-test") {
                        call.respondText(status = HttpStatusCode.OK, text = "You're good")
                    }
                }
            }

        }

        assertFailsWith<AuthenticationException> {
            testHttpClient.get("/any-test")
        }
    }

    @Test
    fun `throws AuthorizationException when user doesn't have sufficient roles`() = testApplication {

        install(Sessions) {
            cookie<RoleUserImp>("user_session")
        }

        install(Authentication) {
            session<RoleUserImp> {
                challenge {

                }
                validate {
                    it
                }
            }
        }

        val testHttpClient = createClient {
            install(HttpCookies)
        }




        routing {
            get("/auth") {
                call.sessions.set(RoleUserImp(roles = setOf()))
            }
            authenticate {
                withAllRoles("ABC") {
                    get("/any-test") {
                        call.respondText(status = HttpStatusCode.OK, text = "You're good")
                    }
                }
            }

        }

        assertFailsWith<AuthorizationException> {
            testHttpClient.get("/auth")
            testHttpClient.get("/any-test")
        }

    }
}