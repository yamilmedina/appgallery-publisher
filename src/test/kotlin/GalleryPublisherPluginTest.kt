import com.android.build.gradle.AppExtension
import com.yamilmedina.gallerypublish.AppGalleryPublisherPlugin
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
    fun pluginDetectsParametersNotSpecified() {
        project.plugins.apply("com.android.application")
        project.plugins.apply(AppGalleryPublisherPlugin::class.java)
        prepareAndroidLibraryProject(project)
        (project as DefaultProject).evaluate()

        //project.tasks.forEach { println(it) }
        //assertParametersSent
    }

    private fun prepareAndroidLibraryProject(project: Project) {
        val extension = project.extensions.getByType(AppExtension::class.java)
        extension.compileSdkVersion(27)

        val manifestFile = File(project.projectDir, "src/main/AndroidManifest.xml")
        manifestFile.parentFile.mkdirs()
        manifestFile.writeText("""<manifest package="com.foo.bar"/>""")
    }
}