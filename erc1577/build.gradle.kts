import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

dependencies {
    implementation("com.github.komputing:khex:${Versions.khex}")
    implementation("com.squareup.okio:okio:${Versions.okio}")
    implementation("com.github.komputing:kbase58:${Versions.base58}")
    implementation("com.github.komputing:kvarint:0.4")

    implementation("com.github.komputing:kotlin-multihash:0.3")
    implementation("com.github.komputing:khex:${Versions.khex}")
}