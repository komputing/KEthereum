plugins {
    id("kethabi")
}

dependencies {
    "implementation"(project(":eip137"))
    "implementation"(project(":erc181"))
    "implementation"(project(":erc634"))

    "testImplementation"(project(":test_data"))
}
