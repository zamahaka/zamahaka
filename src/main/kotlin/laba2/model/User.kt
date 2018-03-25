package laba2.model

import java.net.URI

data class User(
        val id: Long,
        val name: String,
        val surName: String,
        @Transient val avatarUri: URI? = null
)

data class BodyUser(
        val name: String?,
        val surName: String?
)