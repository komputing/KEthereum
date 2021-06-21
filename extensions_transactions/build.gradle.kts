dependencies {
    implementation(project(":extensions_kotlin"))
    implementation(project(":model"))
    implementation(project(":rlp"))
    implementation(project(":types"))
    implementation(project(":eip1559_detector"))
    implementation(project(":method_signatures"))
    implementation(project(":keccak_shortcut"))
    implementation("com.github.komputing:khex:${Versions.khex}")

    testImplementation("com.beust:klaxon:${Versions.klaxon}")
    testImplementation(project(":erc681"))
}
