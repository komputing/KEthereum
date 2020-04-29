dependencies {

    implementation(project(":abi_filter"))
    implementation(project(":model"))
    implementation(project(":types"))
    implementation(project(":metadata"))
    implementation(project(":metadata_repo"))

    implementation(project(":erc55"))
    implementation(project(":erc681"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

    implementation(project(":method_signatures"))
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")

    api("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation(project(":abi"))


    testImplementation("com.github.komputing:khex:${Versions.khex}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation(project(":test_data"))
}
