dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":rlp"))
    "implementation"(project(":keccak_shortcut"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"("com.beust:klaxon:${Versions.klaxon}")
    "testImplementation"(project(":test_data"))
}
