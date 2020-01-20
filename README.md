# Gallery Publisher Gradle Plugin

`Gallery Publisher` is a Gradle plugin that allows the publication of an artifact in Huawei's AppGallery store. 

## Usage

Add the repository to your buildscript block:
```
buildscript {
  repositories {
    maven {
        url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.yamilmedina:gallery-publisher:<version>"
  }
}
```

Then add your configuration accordingly:
```
galleryPublisher {
    appId = "YOUR_APP_ID_FROM_CONSOLE"
    clientId = "YOUR_CLIENT_ID_FROM_CONSOLE"
    clientSecret = "YOUR_CLIENT_SECRET_FROM_CONSOLE"
    artifactPath = "$project.rootDir/app/build/outputs/apk/prod/release/app-prod-release.apk"
}
```

## Example

WIP

## License

Free use of Gallery Publisher Gradle is permitted under the guidelines and in accordance with the [GNU General Public License v3.0][1] 

[1]: https://opensource.org/licenses/GPL-3.0
