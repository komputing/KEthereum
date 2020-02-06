dependencies {
    "implementation"(project(":extensions_kotlin"))
    "implementation"(project(":model"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"(project(":test_data"))
}
