dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":functions"))
    "implementation"(project(":keccak_shortcut"))
    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"(project(":crypto_impl_spongycastle"))
}
