plugins {
    id("kethabi")
}

dependencies {
    implementation(project(":eip137"))
    implementation(project(":erc181"))
    implementation(project(":erc634"))

    implementation(project(":model"))
    implementation(project(":extensions_kotlin"))

    testImplementation(project(":test_data"))
}
