import com.android.build.gradle.AppExtension
import com.yamilmedina.gallerypublish.AppGalleryPublisherExtension
import com.yamilmedina.gallerypublish.AppGalleryPublisherPlugin
import junit.framework.Assert.assertNotNull
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
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
    fun pluginIsAddedSuccessfullyToProject() {
        project.plugins.apply("com.android.application")
        prepareAndroidLibraryProject(project)
        project.plugins.apply(AppGalleryPublisherPlugin::class.java)
        prepareAppGalleryPublishPlugin(project)

        (project as DefaultProject).evaluate()

        val task = project.getTasksByName("appGalleryPublishRelease", false)
        assertNotNull(task)
    }

    private fun prepareAndroidLibraryProject(project: Project) {
        val extension = project.extensions.getByType(AppExtension::class.java)
        extension.compileSdkVersion(27)

        val manifestFile = File(project.projectDir, "src/main/AndroidManifest.xml")
        manifestFile.parentFile.mkdirs()
        manifestFile.writeText("""<manifest package="com.foo.bar"/>""")
    }

    private fun prepareAppGalleryPublishPlugin(project: Project) {
        val extension = project.extensions.getByType(AppGalleryPublisherExtension::class.java)
        extension.appId = "123456"
        extension.clientId = "0987654321"
        extension.clientSecret = "MF29J23D2K23FAD023D2DK23DL222221"
        extension.artifactPath = "/tmp/app.apk"
    }
}