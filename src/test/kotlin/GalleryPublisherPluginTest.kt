import com.android.build.gradle.AppExtension
import com.yamilmedina.gallerypublish.AppGalleryPublisherExtension
import com.yamilmedina.gallerypublish.AppGalleryPublisherPlugin
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

    lateinit var project: Project

    @Before
    fun init() {
        project = ProjectBuilder.builder()
            .withName("sample-app")
            .build()
    }

    @Test(expected = PluginApplicationException::class)
    fun pluginDetectsAndroidPluginNotApplied() {
        project.pluginManager.apply(AppGalleryPublisherPlugin::class.java)
    }

    @Test
    fun pluginDetectsParamsNotDefined() {
        try {
            project.plugins.apply("com.android.application")
            prepareAndroidLibraryProject(project)
            project.plugins.apply(AppGalleryPublisherPlugin::class.java)
            prepareAppGalleryPublishPlugin(project, null, "123412341241", "WAGEBUFS13BFAJS1", "/tmp/app.apk")

            (project as DefaultProject).evaluate()

            project.getTasksByName("appGalleryPublishRelease", false)
            project.extensions.getByType(AppGalleryPublisherExtension::class.java)
        } catch (ex: Exception) {
            assertThat(ex.cause?.message, `is`("Param appId is required"))
        }
    }

    @Test
    fun pluginTaskIsAddedSuccessfullyToProject() {
        project.plugins.apply("com.android.application")
        prepareAndroidLibraryProject(project)
        project.plugins.apply(AppGalleryPublisherPlugin::class.java)
        prepareAppGalleryPublishPlugin(project, "123456", "123412341241", "WAGEBUFS13BFAJS1", "/tmp/app.apk")

        (project as DefaultProject).evaluate()

        val task = project.getTasksByName("appGalleryPublishRelease", false)
        assertNotNull(task)
    }

    private fun prepareAndroidLibraryProject(project: Project) {
        val extension = project.extensions.getByType(AppExtension::class.java)
        extension.compileSdkVersion(28)

        val manifestFile = File(project.projectDir, "src/main/AndroidManifest.xml")
        manifestFile.parentFile.mkdirs()
        manifestFile.writeText("""<manifest package="com.foo.bar"/>""")
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