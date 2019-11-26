plugins {
    idea
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = false
    }
}