package com.yamilmedina.gallerypublish

import com.android.build.gradle.AppExtension
import com.android.builder.model.Version
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AppGalleryPublisherPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val galleryPublisherExtension =
            project.extensions.create(EXTENSION_NAME, AppGalleryPublisherExtension::class.java)

        val androidAppExtension = project.requireAndroidAppExtension()
        androidAppExtension.applicationVariants.whenObjectAdded {
            if (it.buildType.isDebuggable) return@whenObjectAdded

            val appVariant = it
            val variantName = appVariant.name.capitalize()

            project.tasks.register("appGalleryPublish$variantName", PublishTask::class.java) { task ->
                task.group = TASK_GROUP
                task.description = "Publish $variantName APK to Huawei's App Gallery."

                galleryPublisherExtension.validateParameters()

                task.appId = galleryPublisherExtension.appId!!
                task.clientId = galleryPublisherExtension.clientId!!
                task.clientSecret = galleryPublisherExtension.clientSecret!!
                task.artifactPath = galleryPublisherExtension.artifactPath!!
            }
        }
    }

    companion object {
        private const val TASK_GROUP = "Publishing"
        private const val PLUGIN_ID = "com.yamilmedina.gallery-publisher"
        private const val EXTENSION_NAME = "galleryPublisher"
        private const val MINIMAL_ANDROID_PLUGIN_VERSION = "3.0.1"

        private fun Project.requireAndroidAppExtension(): AppExtension {
            val current = Version.ANDROID_GRADLE_PLUGIN_VERSION
            val expected = MINIMAL_ANDROID_PLUGIN_VERSION
            if (current < expected) {
                error(
                    "Plugin '$PLUGIN_ID' requires 'com.android.application' plugin version $expected or higher," +
                            " while yours is $current. Update android gradle plugin and try again."
                )
            }
            return project.extensions.findByType(AppExtension::class.java)
                ?: error("Required 'com.android.application' plugin must be added prior '$PLUGIN_ID' plugin.")
        }

        private fun AppGalleryPublisherExtension.validateParameters() {
            if (this.appId == null) {
                throw IllegalArgumentException("Param appId is required")
            }
            if (this.clientId == null) {
                throw IllegalArgumentException("Param clientId is required")
            }
            if (this.clientSecret == null) {
                throw IllegalArgumentException("Param clientSecret is required")
            }
            if (this.artifactPath == null) {
                throw IllegalArgumentException("Param artifactPath is required")
            }
        }
    }
}