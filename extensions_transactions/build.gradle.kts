dependencies {
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":model"))
    "implementation"(project(":rlp"))
    "implementation"(project(":keccak_shortcut"))
    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"("com.beust:klaxon:${Versions.klaxon}")
}
