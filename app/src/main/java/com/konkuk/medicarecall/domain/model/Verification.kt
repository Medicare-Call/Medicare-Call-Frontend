package com.konkuk.medicarecall.domain.model

sealed class Verification(
    open val verified: Boolean,
    open val message: String,
) {
    data class NewMember(
        override val verified: Boolean = false,
        override val message: String = "",
        val newUserToken: String = "",
        val memberStatus: MemberStatus = MemberStatus.NEW_MEMBER,
    ) : Verification(verified, message)

    data class ExistingMember(
        override val verified: Boolean = true,
        override val message: String = "",
        val accessToken: String = "",
        val refreshToken: String = "",
        val memberStatus: MemberStatus = MemberStatus.EXISTING_MEMBER,
    ) : Verification(verified, message)
}

enum class MemberStatus {
    NEW_MEMBER,
    EXISTING_MEMBER,
    ;

    companion object {
        fun fromString(status: String): MemberStatus {
            return when (status) {
                "NEW_MEMBER" -> NEW_MEMBER
                "EXISTING_MEMBER" -> EXISTING_MEMBER
                else -> NEW_MEMBER
            }
        }
    }
}
