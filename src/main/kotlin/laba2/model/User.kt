package laba2.model

data class User(
        val id: Long,
        val name: String,
        val surName: String
)

data class BodyUser(
        val name: String?,
        val surName: String?
)