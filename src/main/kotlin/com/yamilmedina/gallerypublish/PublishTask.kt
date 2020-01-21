package com.yamilmedina.gallerypublish

import com.yamilmedina.gallerypublish.api.GetToken
import com.yamilmedina.gallerypublish.api.UploadAppDistributable
import com.yamilmedina.gallerypublish.api.UploadAppFileInfo
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

internal open class PublishTask : DefaultTask() {

    private val logger by lazy { LoggerFactory.getLogger(this.javaClass.name) }
    private val tokenApi by lazy { GetToken(clientId, clientSecret) }
    private val uploadAppApi by lazy { UploadAppDistributable(appId, clientId, artifactPath) }
    private val uploadAppFileInfo by lazy { UploadAppFileInfo(appId, clientId) }

    @get:Input
    lateinit var appId: String
    @get:Input
    lateinit var clientId: String
    @get:Input
    lateinit var clientSecret: String
    @get:Input
    lateinit var artifactPath: String

    @TaskAction
    fun execute() {
        val token = tokenApi.getToken()
        val uploadApkResponse = uploadAppApi.uploadApp(token)

        if (uploadApkResponse.result.uploadFileRsp?.ifSuccess != 1) {
            logger.error("Could not upload the APK, please run the task again with --debug to get more insights.")
            return
        }

        val uploadedStatus = uploadAppFileInfo.updateAppFileInfo(
            token,
            uploadApkResponse.result.uploadFileRsp.fileInfoList
        )

        if (!uploadedStatus) {
            throw GradleException("The APK could not be saved into HMS console, please run the task again with --debug to get more insights.")
        }
    }


}