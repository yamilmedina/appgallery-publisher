package com.yamilmedina.gallerypublish.api

import com.google.gson.annotations.SerializedName

data class UploadFileRsp(
    @SerializedName("fileInfoList") val fileInfoList: List<FileInfoList>,
    @SerializedName("ifSuccess") val ifSuccess: Int
)