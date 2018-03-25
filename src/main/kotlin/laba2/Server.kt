package laba2

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import laba2.extentions.respondMissingProperties
import laba2.extentions.respondMissingProperty
import laba2.extentions.respondNotFound
import laba2.model.BodyUser
import laba2.model.User
import org.slf4j.event.Level


private val users = mutableMapOf<Long, User>()

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging) { level = Level.INFO }
    install(ContentNegotiation) { gson { setPrettyPrinting() } }
    routing {
        installUserRouting()
    }
}

fun Routing.installUserRouting() = route("/users") {

    get { call.respond(users.values) }

    post("/{id}") {
        val id = call.parameters["id"]?.toLong() ?: 0

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        val bodyUser = call.receive<BodyUser>()

        if (bodyUser.name == null && bodyUser.surName == null) call.respondMissingProperties(
                listOf(BodyUser::name.name, BodyUser::surName.name)
        )

        var updatedUser = user
        if (bodyUser.name != null) updatedUser = user.copy(name = bodyUser.name)
        if (bodyUser.surName != null) updatedUser = updatedUser.copy(surName = bodyUser.surName)

        users[id] = updatedUser

        call.respond(HttpStatusCode.Accepted, updatedUser)
    }

    put {
        val bodyUser = call.receive<BodyUser>()

        val user = User(
                id = System.currentTimeMillis(),
                name = bodyUser.name ?: call.respondMissingProperty(BodyUser::name.name),
                surName = bodyUser.surName ?: call.respondMissingProperty(BodyUser::surName.name)
        )
        users[user.id] = user

        call.respond(HttpStatusCode.Created, user)
    }

    delete("/{id}") {
        val id = call.parameters["id"]?.toLong() ?: 0

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        users.remove(user.id)

        call.respond(HttpStatusCode.Accepted, "")
    }
}