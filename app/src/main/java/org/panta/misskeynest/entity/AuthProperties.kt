package org.panta.misskeynest.entity

data class SessionResponse(val token: String, val url: String)
data class UserKeyResponse(val accessToken: String, val user: User)