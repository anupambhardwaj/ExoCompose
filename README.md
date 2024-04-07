# ExoCompose
Media player library for Jetpack Compose and Media3 Library

# Download

Add the following line of code in your project level `settings.gradle.kts` file

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the following dependency in your app level `build.gradle` file
```
dependencies {
  implementation 'com.github.anupambhardwaj:ExoCompose:0.1.0'
}
```
