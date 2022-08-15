dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":rlp"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"("com.github.komputing:khex:${Versions.khex}")
    "testImplementation"(project(":crypto_impl_spongycastle"))
}
