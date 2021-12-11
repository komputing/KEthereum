plugins {
    application
}

application {
    mainClassName = "org.kethereum.example_cli.ExampleCLIKt"
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

dependencies {
    "implementation"(project(":model"))
    "implementation"(project(":flows"))
    "implementation"(project(":erc55"))
    "implementation"(project(":eip137"))
    "implementation"(project(":rpc"))
    "implementation"(project(":rpc_min3"))
    "implementation"(project(":ens"))
    "implementation"(project(":test_data"))
    "implementation"(project(":abi"))
    "implementation"(project(":abi_codegen"))

    "implementation"("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    "implementation"("com.squareup.okio:okio:${Versions.okio}")
}
