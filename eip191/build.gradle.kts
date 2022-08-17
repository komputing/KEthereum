dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":rlp"))

    "testImplementation"(project(":crypto_impl_spongycastle"))
}
