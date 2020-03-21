dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation(project(":model"))
    implementation(project(":rpc"))
}