dependencies {
    "implementation"(project(":model"))
    "implementation"("com.github.komputing:khex:${Versions.khex}")
}


val copyTestResources: Task = tasks.create<Copy>("copyTestResources") {
    description = """
        Copies resources from src/test/resources to build/classes/java/test so they are accessible by test classes
        """.trim()
    from("$projectDir/src/main/resources")
    into("$buildDir/classes/java/test")
}

tasks.withType<Test> {
    dependsOn(copyTestResources)
}