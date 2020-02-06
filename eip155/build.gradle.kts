dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":extensions_transactions"))
    "implementation"(project(":rlp"))

    "testImplementation"("com.github.komputing:khex:${Versions.khex}")
    "testImplementation"(project(":crypto_impl_spongycastle"))
    "testImplementation"("com.beust:klaxon:${Versions.klaxon}")
}
