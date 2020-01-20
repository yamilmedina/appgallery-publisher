package com.yamilmedina.gallerypublish

import com.yamilmedina.gallerypublish.api.GetAppInfo
import com.yamilmedina.gallerypublish.api.GetToken
import com.yamilmedina.gallerypublish.api.UploadAppDistributable
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

internal open class PublishTask : DefaultTask() {

    private val logger by lazy { LoggerFactory.getLogger(this.javaClass.name) }
    private val tokenApi by lazy { GetToken(clientId, clientSecret) }
    private val appInfoApi by lazy { GetAppInfo(appId, clientId) }
    private val uploadAppApi by lazy { UploadAppDistributable(appId, clientId, artifactPath) }

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
        logger.error("EL TOKEN -----> $token")
        val appInfo = appInfoApi.getAppInfo(token)
        val uploaded = uploadAppApi.uploadApp(token)
        logger.error("VALIDO ? -----> $uploaded")

//        // 更新应用信息
//        UpdateAppInfo.updateAppInfo(domain, clientId, token, appId);
//
//        // 上传文件
//        List<FileInfo> files = UploadFile . uploadFile (domain, clientId, token, appId, "png");
//
//        // 上传文件后必须调用更新应用文件信息接口
//        UploadAppFileInfo.updateAppFileInfo(domain, clientId, token, appId, files);
//
//        // 提交审核
//        SubmitAudit.submit(domain, clientId, token, appId);
    }


}