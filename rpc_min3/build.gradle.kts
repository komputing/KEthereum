dependencies {
    "implementation"(project(":rpc"))
    "api"("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    "api"("com.squareup.moshi:moshi:${Versions.moshi}")

    "testImplementation"("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    "testImplementation"("org.threeten:threetenbp:1.4.1")
}
