dependencies {
    "implementation"(project(":crypto"))
    "implementation"(project(":crypto_api"))
    "implementation"(project(":extensions"))
    "implementation"(project(":model"))

    "implementation"("com.github.komputing:kbip44:0.1")
    "implementation"("com.github.komputing:kbase58:0.1")
    "implementation"("com.github.komputing.khash:sha256:${Versions.khash}")
    "implementation"("com.github.komputing.khash:ripemd160:${Versions.khash}")

    testImplementation("com.github.komputing:khex:${Versions.khex}")
    testImplementation(project(":crypto_impl_bouncycastle"))

    testImplementation(project(":test_data"))
}
