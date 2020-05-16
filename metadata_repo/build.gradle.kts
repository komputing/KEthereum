dependencies {
    implementation(project(":metadata_model"))
    implementation(project(":model"))
    implementation(project(":erc55"))
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")

    testImplementation(project(":test_data"))
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
}
