plugins {
    kotlin("kapt")
}

dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    api("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation(project(":abi"))
    implementation(project(":extensions_kotlin"))
    testImplementation(project(":test_data"))
}
