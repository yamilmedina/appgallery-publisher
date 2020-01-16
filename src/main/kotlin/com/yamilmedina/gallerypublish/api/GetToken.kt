package com.yamilmedina.gallerypublish.api

import kong.unirest.Unirest

internal class GetToken(
    private val clientId: String,
    private val clientSecret: String
) {

    fun getToken(): String {
        val response = Unirest.post("https://connect-api.cloud.huawei.com/api/oauth2/v1/token")
            .header("Content-Type", "application/json")
            .header("cache-control", "no-cache")
            .body(TokenRequest(clientId, clientSecret, GRANT_TYPE))
            .asObject(TokenResponse::class.java)

        return response.body.access_token
    }

    data class TokenResponse(var access_token: String = "", var expires_in: Long = 0)
    data class TokenRequest(
        var client_id: String,
        var client_secret: String,
        var grant_type: String = GRANT_TYPE
    )

    companion object {
        private const val GRANT_TYPE = "client_credentials"
    }
}