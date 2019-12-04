dependencies {
    "implementation"(project(":abi"))
    "implementation"(project(":keccak_shortcut"))
    "implementation"(project(":types"))
    "implementation"(project(":model"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")
    "implementation"("com.squareup.okhttp3:okhttp:${Versions.okhttp}")

    "testImplementation"(project(":crypto_impl_spongycastle"))
    "testImplementation"("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    "testImplementation"(project(":test_data"))
}
