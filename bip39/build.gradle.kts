dependencies {
    "implementation"(project(":bip32"))
    "implementation"(project(":extensions"))
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")
    "implementation"("com.github.komputing.khash:sha256:${Versions.khash}")

    "testImplementation"(project(":bip39_wordlist_en"))
    "testImplementation"(project(":crypto_impl_spongycastle"))
}
