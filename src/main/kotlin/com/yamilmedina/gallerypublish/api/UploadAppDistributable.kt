package com.yamilmedina.gallerypublish.api

import kong.unirest.Unirest
import java.io.File

internal class UploadAppDistributable(
    private val appId: String,
    private val clientId: String,
    private val apkFile: File
) {

    fun uploadApp(token: String): Boolean {
        val (uploadUrl, authCode) = getUploadUlr(token)
        return uploadUrl.contains("uploadFile")
    }

    private fun getUploadUlr(token: String): UrlAppResponse {
        val response =
            Unirest.get("https://connect-api.cloud.huawei.com/api/publish/v2/upload-url")
                .header("client_id", clientId)
                .header(
                    "Authorization",
                    "Bearer $token"
                )
                .queryString("appId", this.appId)
                .queryString("suffix", "apk")
                .asObject(UrlAppResponse::class.java)

        return response.body
    }

    data class UrlAppResponse(val uploadUrl: String, val authCode: String)

}