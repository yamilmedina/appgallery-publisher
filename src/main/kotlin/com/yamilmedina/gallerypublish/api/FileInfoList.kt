package com.yamilmedina.gallerypublish.api

import com.google.gson.annotations.SerializedName

data class FileInfoList(
    @SerializedName("fileDestUlr") val fileDestUlr: String,
    @SerializedName("size") val size: Int
)
