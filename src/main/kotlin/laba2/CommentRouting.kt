package laba2

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import laba2.extentions.respondMissingProperty
import laba2.extentions.respondNotFound
import laba2.model.ApiComment
import laba2.model.Comment

fun Routing.installCommentRouting() = route("/comments") {

    get("/all/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: run {
            call.respond(emptyList<Comment>())
            return@get
        }

        val commentsForUser = comments.values.filter { it.userId == id }

        call.respond(commentsForUser)
    }

    post("edit/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val comment = comments[id] ?: call.respondNotFound("Comment with id: $id not found")

        val apiComment = call.receive<ApiComment>()

        val edited = comment.copy(text = apiComment.text ?: call.respondMissingProperty(ApiComment::text.name))

        comments[edited.id] = edited

        call.respond(HttpStatusCode.Accepted, edited)
    }

    put("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest, Error("No recipient specified"))
            return@put
        }

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        val apiComment = call.receive<ApiComment>()

        val comment = Comment(
                id = System.currentTimeMillis(),
                userId = user.id,
                text = apiComment.text ?: call.respondMissingProperty(ApiComment::text.name)
        )

        comments[comment.id] = comment

        call.respond(HttpStatusCode.Created, comment)
    }

    delete("/all/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        comments.filterValues { it.userId == id }.forEach { key, _ -> comments.remove(key) }

        call.respond(HttpStatusCode.Accepted)
    }

    delete("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        comments.remove(id)
                ?.also { call.respond(HttpStatusCode.Accepted) }
                ?: call.respond(HttpStatusCode.NotFound)
    }


}