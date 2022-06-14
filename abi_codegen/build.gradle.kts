dependencies {
    "implementation"(project(":abi"))
    "implementation"(project(":abi_filter"))
    "implementation"(project(":rpc"))
    "implementation"(project(":model"))
    "implementation"(project(":method_signatures"))
    "implementation"(project(":types"))

    "implementation"("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")

    "api"("com.squareup.moshi:moshi:${Versions.moshi}")
    "api"("com.squareup:kotlinpoet:1.12.0")

    "testImplementation"(project(":test_data"))
    "testImplementation"(project(":extensions_kotlin"))
}
