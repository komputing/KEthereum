dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":functions"))
    "implementation"(project(":extensions"))

    "implementation"("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    "implementation"("com.squareup.moshi:moshi:${Versions.moshi}")

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    "testImplementation"("org.threeten:threetenbp:1.4.0")
}
