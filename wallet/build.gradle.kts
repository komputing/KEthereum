dependencies {
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))
    "implementation"(project(":keccak_shortcut"))
    "implementation"(project(":extensions"))
    "implementation"(project(":model"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "implementation"("com.squareup.moshi:moshi:${Versions.moshi}")
    "implementation"("com.squareup.moshi:moshi-adapters:${Versions.moshi}")

    "testImplementation"(project(":crypto_impl_spongycastle"))
}
