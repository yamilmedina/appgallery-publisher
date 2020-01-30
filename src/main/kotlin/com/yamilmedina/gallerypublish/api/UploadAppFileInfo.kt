package com.yamilmedina.gallerypublish.api

import com.google.gson.annotations.SerializedName
import com.yamilmedina.gallerypublish.config.UnirestClient
import org.slf4j.LoggerFactory

internal class UploadAppFileInfo(
    private val unirestClient: UnirestClient,
    private val appId: String,
    private val clientId: String
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun updateAppFileInfo(token: String, files: List<FileInfoList>): Boolean {
        val response = unirestClient.put("https://connect-api.cloud.huawei.com/api/publish/v2/app-file-info")
            .header("client_id", clientId)
            .header(
                "Authorization",
                "Bearer $token"
            )
            .header("Content-Type", "application/json")
            .queryString("appId", appId)
            .body(UpdateAppFileRequest(FILE_TYPE_APK, LANGUAGE_ES_LA, files.map {
                FileInfoListPublish(
                    "app-prod-release.apk",
                    it.fileDestUlr,
                    it.size
                )
            }))
            .asObject(UploadAppFileInfoResponse::class.java)

        return response.body.data.code == 0
    }

    data class UpdateAppFileRequest(
        @SerializedName("fileType") val fileType: Int = FILE_TYPE_APK,
        @SerializedName("lang") val lang: String = LANGUAGE_ES_LA,
        @SerializedName("files") val files: List<FileInfoListPublish>
    )

    data class FileInfoListPublish(
        @SerializedName("fileName") val fileName: String,
        @SerializedName("fileDestUrl") val fileDestUrl: String,
        @SerializedName("size") val size: Int
    )

    data class UploadAppFileInfoResponse(
        @SerializedName("ret") val data: UploadAppFileInfoResponseData
    )

    data class UploadAppFileInfoResponseData(
        @SerializedName("code") val code: Int,
        @SerializedName("msg") val message: String
    )

    companion object {
        const val FULL_RELEASE = 1
        const val PHASED_RELEASE = 3
        const val FILE_TYPE_APK = 5
        const val LANGUAGE_ES_LA = "es-419"
    }

}
