dependencies {
    implementation(project(":crypto"))
    implementation(project(":crypto_api"))
    implementation(project(":extensions_kotlin"))
    implementation(project(":model"))
    implementation("com.github.komputing:kbip44:0.5")
    implementation("com.github.komputing:kbase58:${Versions.base58}")
    implementation("com.github.komputing.khash:sha256:${Versions.khash}")
    implementation("com.github.komputing.khash:ripemd160:${Versions.khash}")

    testImplementation("com.github.komputing:khex:${Versions.khex}")
    testImplementation(project(":crypto_impl_bouncycastle"))

    testImplementation(project(":test_data"))
}
