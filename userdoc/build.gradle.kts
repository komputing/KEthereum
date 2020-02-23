dependencies {

    implementation(project(":abi_filter"))
    implementation(project(":model"))
    implementation(project(":metadata"))
    implementation(project(":erc55"))
    implementation(project(":erc681"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    implementation(project(":method_signatures"))
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")

    api("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation(project(":abi"))


    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation(project(":test_data"))
}
