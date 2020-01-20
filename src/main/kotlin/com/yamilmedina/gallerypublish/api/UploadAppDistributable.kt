package com.yamilmedina.gallerypublish.api

import kong.unirest.MultipartMode
import kong.unirest.Unirest
import org.slf4j.LoggerFactory
import java.io.File

internal class UploadAppDistributable(
    private val appId: String,
    private val clientId: String,
    private val artifactPath: String
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun uploadApp(token: String): UploadApkResponse {
        val apk = File(artifactPath)
        val (uploadUrl, authCode) = getUploadUlr(token)

        return uploadFile(uploadUrl, authCode, apk)
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

    private fun uploadFile(uploadUrl: String, authCode: String, apk: File): UploadApkResponse {
        try {
            val response = Unirest.post(uploadUrl)
                .multiPartContent()
                .mode(MultipartMode.BROWSER_COMPATIBLE)
                .field("fileCount", "1")
                .field("authCode", authCode)
                .field("file", apk.inputStream(), apk.name)
                .field("name", apk.name)
                .uploadMonitor { _, fileName, bytesWritten, totalBytes ->
                    logger.info("Uploading $fileName - $bytesWritten of $totalBytes")
                }
                .asObject(UploadApkResponse::class.java)

            return response.body
        } catch (ex: Exception) {
            logger.error("Error: ${ex.message}", ex)
            return UploadApkResponse(Result(null, 1))
        }
    }

    data class UrlAppResponse(val uploadUrl: String, val authCode: String, val chunkUploadUrl: String)

}