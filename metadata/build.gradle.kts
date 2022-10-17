plugins {
    kotlin("plugin.serialization") version "${Versions.kotlin}"
}

dependencies {

    implementation(project(":abi_filter"))
    implementation(project(":model"))
    implementation(project(":types"))
    implementation(project(":metadata_model"))
    implementation(project(":metadata_repo"))

    implementation(project(":erc55"))
    implementation(project(":erc681"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

    implementation(project(":method_signatures"))
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")

    api("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation(project(":abi"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.4.1")
    implementation("com.github.komputing:kotlin-multihash:0.3")
    implementation("com.github.komputing:khex:${Versions.khex}")

    testImplementation("com.github.komputing:khex:${Versions.khex}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation(project(":test_data"))
}
