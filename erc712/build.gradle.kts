plugins {
    kotlin("kapt")
}

dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    implementation(project(":model"))
    implementation(project(":crypto"))
    implementation(project(":crypto_api"))
    implementation(project(":extensions_kotlin"))
    implementation(project(":extensions_transactions"))
    implementation(project(":rlp"))
    implementation(project(":types"))
    implementation(project(":keccak_shortcut"))
    implementation("com.github.komputing:khex:${Versions.khex}")
    implementation("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation("com.squareup.okio:okio:${Versions.okio}")

    testImplementation(project(":crypto_impl_bouncycastle"))

}
