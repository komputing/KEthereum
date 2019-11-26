apply {
    from("https://raw.githubusercontent.com/ligi/gradle-common/master/versions_plugin_stable_only.gradle")
}

buildscript {
    repositories {
        jcenter()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.versions_plugin}")
        classpath("com.github.komputing:kethabi:0.0.9")
    }
}


subprojects {
    repositories {
        jcenter()
        maven("https://jitpack.io")
        maven("https://kotlin.bintray.com/kotlinx")
    }

    apply(plugin = "jacoco")
    apply(plugin = "maven")
    apply(plugin = "kotlin")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }

    afterEvaluate {

        dependencies {
            "implementation"("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

            "testImplementation"("org.assertj:assertj-core:3.14.0")
            "testImplementation"("org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}")
            "testRuntime"("org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}")

            "testImplementation"("org.jetbrains.kotlin:kotlin-test")
            "testImplementation"("io.mockk:mockk:1.9.3")
        }


    }
}
