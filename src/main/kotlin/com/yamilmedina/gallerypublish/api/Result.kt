package com.yamilmedina.gallerypublish.api

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("UploadFileRsp") val uploadFileRsp: UploadFileRsp?,
    @SerializedName("resultCode") val resultCode: Int
)