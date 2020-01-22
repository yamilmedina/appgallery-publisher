import com.yamilmedina.gallerypublish.AppGalleryPublisherPlugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AppGalleryPublisherPluginTest {

    private Project project
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Before
    void init() {
        project = ProjectBuilder.builder()
                .withName("sample-app")
                .withProjectDir(temporaryFolder.newFolder("sample-app", "app"))
                .build()
    }

    @Test(expected = PluginApplicationException.class)
    void pluginDetectsAndroidPluginNotApplied() {
        project.pluginManager.apply(AppGalleryPublisherPlugin.class)
    }

    @Test
    void pluginAppliesTaskToPublishToAppGallery() {
        project = createProject()
        project.evaluate()

        project.configurations.forEach({ p -> println(p) })
        project.configurations.findByName("appGalleryPublishRelease").forEach({ p -> println(p) })
    }

    private static Project createProject() {
        final Project project = ProjectBuilder.builder().build()
        project.apply plugin: "com.android.application"
        project.apply plugin: "com.yamilmedina.gallery-publisher"
        project.android {
            compileSdkVersion 27

            defaultConfig {
                applicationId "com.example"
            }
        }
        return project
    }
}