package com.example.htseafood.model.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("accountLocked")
    val accountLocked: Boolean? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("lastLoginDateTime")
    val lastLoginDateTime: Long? = null,

    @SerializedName("hoursSavedPerDay")
    val hoursSavedPerDay: Int? = null,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("createDateTime")
    val createDateTime: Long? = null,

    @SerializedName("dollarsSavedPerDay")
    val dollarsSavedPerDay: Int? = null,

    @SerializedName("authorizationToken")
    val authorizationToken: String? = null,

    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("userRoleId")
    val userRoleId: Int? = null,

    @SerializedName("socialType")
    val socialType: Int? = null,

    @SerializedName("updateDateTime")
    val updateDateTime: Long? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("sobrietyDateTime")
    var sobrietyDateTime: Long? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
)
