import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += listOf("-XXLanguage:+InlineClasses","-Xopt-in=kotlin.ExperimentalUnsignedTypes")
}

dependencies {
    implementation("com.github.komputing:khex:${Versions.khex}")
    implementation("com.squareup.okio:okio:${Versions.okio}")
    implementation("com.github.komputing:kbase58:${Versions.base58}")
    implementation("com.github.komputing:kvarint:0.2")
    implementation("com.github.multiformats:java-multihash:1.2.1")
    implementation("com.github.komputing:khex:1.0.0-RC6")
}