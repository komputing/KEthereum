dependencies {
    "implementation"(project(":abi"))
    "implementation"(project(":rpc"))
    "implementation"(project(":model"))
    "implementation"(project(":method_signatures"))
    "implementation"(project(":types"))

    "implementation"("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")

    "api"("com.squareup.moshi:moshi:${Versions.moshi}")
    "api"("com.squareup:kotlinpoet:1.4.4")

    "testImplementation"(project(":test_data"))
}
