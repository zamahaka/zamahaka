package laba2.extentions

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import laba2.model.Error

suspend fun ApplicationCall.respondNotFound(message: String = ""): Nothing {
    respond(HttpStatusCode.NotFound, Error(message))

    throw IllegalArgumentException("Not found error")
}

suspend fun ApplicationCall.respondMissingProperty(propName: String): Nothing {
    respond(HttpStatusCode.BadRequest, Error("Missing property $propName"))

    throw IllegalArgumentException("Call with missing property $propName")
}

suspend fun ApplicationCall.respondMissingProperties(propsName: List<String>): Nothing {
    respond(HttpStatusCode.BadRequest, Error("Missing properties $propsName"))

    throw IllegalArgumentException("Call with missing property $propsName")
}