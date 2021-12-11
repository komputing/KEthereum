plugins {
    kotlin("kapt")
}

dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    implementation(project(":model"))
    implementation(project(":extensions_kotlin"))

    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.squareup.moshi:moshi:${Versions.moshi}")

    implementation("com.github.komputing:khex:${Versions.khex}")

    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation("org.threeten:threetenbp:${Versions.threetenbp}")
}
