dependencies {
    implementation(project(":rpc"))
    implementation("com.github.komputing:khex:${Versions.khex}")
    implementation(project(":extensions_kotlin"))

    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
}
