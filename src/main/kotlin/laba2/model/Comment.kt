package laba2.model

data class Comment(
        val id: Long,
        val userId: Long,
        val text: String
)

data class ApiComment(val text: String?)