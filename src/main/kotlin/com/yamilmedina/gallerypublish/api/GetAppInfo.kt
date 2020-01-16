package com.yamilmedina.gallerypublish.api

import kong.unirest.Unirest

internal class GetAppInfo(
    private val appId: String,
    private val clientId: String
) {

    fun getAppInfo(token: String): String {
        val response = Unirest.get("https://connect-api.cloud.huawei.com/api/publish/v2/app-info")
            .header("client_id", clientId)
            .header(
                "Authorization",
                "Bearer $token"
            )
            .queryString("appid", appId)
            .asString()

        return response.body.toString()
    }
}