package laba2

import io.ktor.application.call
import io.ktor.content.PartData
import io.ktor.content.forEachPart
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondRedirect
import io.ktor.routing.*
import laba2.extentions.respondNotFound
import java.io.File

fun Routing.installAvatarRouting() = route("/avatars") {

    get("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: 0

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        val avatarFile = File(user.avatarUri ?: call.respondNotFound("user does not have avatar"))

        call.respondFile(avatarFile)
    }

    post("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: 0

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        var avatarFile: File? = null

        call.receiveMultipart().forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                }
                is PartData.FileItem -> {
                    val ext = File(part.originalFileName).extension
                    val file = File(System.getProperty("user.dir"), "avatar_${user.id}.$ext")

                    part.streamProvider().use { incomingStream ->
                        file.outputStream().buffered().use { incomingStream.copyTo(it) }
                    }

                    avatarFile = file
                }
            }

            part.dispose()
        }

        users[user.id] = user.copy(avatarUri = avatarFile?.toURI())

        call.respondRedirect("/avatars/${user.id}")
    }

    delete("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: 0

        val user = users[id] ?: call.respondNotFound("User with id: $id not found")

        val avatarFile = File(user.avatarUri ?: call.respondNotFound("user does not have avatar"))

        avatarFile.delete()

        users[user.id] = user.copy(avatarUri = null)

        call.respond(HttpStatusCode.Accepted)
    }

}