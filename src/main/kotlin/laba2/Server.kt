package laba2

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.routing
import laba2.model.Comment
import laba2.model.User
import org.slf4j.event.Level
import java.lang.reflect.Modifier

val users = mutableMapOf<Long, User>()
val comments = mutableMapOf<Long, Comment>()

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging) { level = Level.INFO }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()

            excludeFieldsWithModifiers(Modifier.TRANSIENT)
        }
    }
    routing {
        installUserRouting()
        installAvatarRouting()
        installCommentRouting()
    }
}