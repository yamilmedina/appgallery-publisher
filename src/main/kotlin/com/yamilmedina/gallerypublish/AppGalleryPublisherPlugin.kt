package com.yamilmedina.gallerypublish

import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AppGalleryPublisherPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.tasks.register("appGalleryPublisher", PublishTask::class.java) {
            it.group = TASK_GROUP
            it.description = "Publish APK to Huawei's App Gallery."
        }
    }

    companion object {
        internal const val TASK_GROUP = "Publishing"
        internal const val PLUGIN_ID = "gallery-publisher"
        internal const val EXTENSION_NAME = "gallery-publisher"
    }
}