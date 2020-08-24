dependencies {
    implementation(project(":model"))
    implementation(project(":uri_common"))
    implementation(project(":erc831"))
    implementation(project(":method_signatures"))
    implementation(project(":types"))

    implementation("com.github.komputing:khex:${Versions.khex}")
}
