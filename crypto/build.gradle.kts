dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":keccak_shortcut"))
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":crypto_api"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "implementation"("org.slf4j:slf4j-api:${Versions.slf4j}")

    "testImplementation"(project(":test_data"))
    "testImplementation"("com.github.komputing.khash:sha256:${Versions.khash}")
    "testImplementation"(project(":crypto_impl_spongycastle"))
}
