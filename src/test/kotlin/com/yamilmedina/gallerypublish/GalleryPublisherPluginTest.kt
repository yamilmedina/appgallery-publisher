package com.yamilmedina.gallerypublish

import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.File

class GalleryPublisherPluginTest {

    private lateinit var project: Project

    @Before
    fun init() {
        project = ProjectBuilder.builder()
            .withName("sample-app")
            .build()
    }

    @Test(expected = PluginApplicationException::class)
    fun `Plugin application fails when android plugin not present`() {
        project.pluginManager.apply(AppGalleryPublisherPlugin::class.java)
    }

    @Test
    fun `Plugin application fails when parameters not defined`() {
        try {
            project.plugins.apply("com.android.application")
            prepareAndroidLibraryProject(project)
            project.plugins.apply(AppGalleryPublisherPlugin::class.java)
            prepareAppGalleryPublishPlugin(project, null, "ANY_ID", "ANY_SECRET", "/tmp/app.apk")

            (project as DefaultProject).evaluate()

            project.getTasksByName("appGalleryPublishRelease", false)
            project.extensions.getByType(AppGalleryPublisherExtension::class.java)
        } catch (ex: Exception) {
            assertThat(ex.cause?.message, `is`("Param appId is required"))
        }
    }

    @Test
    fun `PublishTask is added successfully to project when configured properly`() {
        project.plugins.apply("com.android.application")
        prepareAndroidLibraryProject(project)
        project.plugins.apply(AppGalleryPublisherPlugin::class.java)
        prepareAppGalleryPublishPlugin(project, "123456", "ANY_ID", "ANY_SECRET", "/tmp/app.apk")

        (project as DefaultProject).evaluate()

        val task = project.getTasksByName("appGalleryPublishRelease", false)
        assertNotNull(task)
    }

    private fun prepareAndroidLibraryProject(project: Project) {
        val extension = project.extensions.getByType(AppExtension::class.java)
        extension.compileSdkVersion(28)

        val manifestFile = File(project.projectDir, "src/main/AndroidManifest.xml")
        manifestFile.parentFile.mkdirs()
        manifestFile.writeText("""<manifest package="com.example.sample-app"/>""")
    }

    private fun prepareAppGalleryPublishPlugin(
        project: Project,
        appId: String?,
        clientId: String?,
        clientSecret: String?,
        artifactPath: String?
    ) {
        val extension = project.extensions.getByType(AppGalleryPublisherExtension::class.java)
        extension.appId = appId
        extension.clientId = clientId
        extension.clientSecret = clientSecret
        extension.artifactPath = artifactPath
    }
}