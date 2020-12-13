plugins {
    id("kethabi")
}

dependencies {
    api(project(":eip137"))
    implementation(project(":erc181"))
    implementation(project(":erc634"))
    api(project(":erc1577"))
    implementation(project(":erc1577_resolver"))

    implementation(project(":model"))
    implementation(project(":extensions_kotlin"))

    testImplementation(project(":test_data"))
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
}
