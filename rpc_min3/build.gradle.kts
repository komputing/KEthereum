plugins {
    kotlin("kapt")
}

dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    implementation(project(":rpc"))
    implementation(project(":model"))
    api("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    api("com.squareup.moshi:moshi:${Versions.moshi}")

    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation("org.threeten:threetenbp:${Versions.threetenbp}")
}
