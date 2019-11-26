dependencies {
    "implementation"(project(":extensions"))
    "implementation"(project(":model"))

    "implementation"("com.github.komputing:khex:${Versions.khex}")

    "testImplementation"(project(":test_data"))
}
