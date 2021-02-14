apply {
    from("https://raw.githubusercontent.com/ligi/gradle-common/master/versions_plugin_stable_only.gradle")
}

buildscript {
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        jcenter()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Versions.versions_plugin}")
        classpath("com.github.fullkomnun:kethabi:${Versions.kethabi_plugin}") {
            isChanging = true
        }
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}

subprojects {
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
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
            "implementation"("com.ionspin.kotlin:bignum-jvm:${Versions.bignum}")

            "testImplementation"("org.assertj:assertj-core:3.19.0")
            "testImplementation"("org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}")
            "testImplementation"("org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}")
            "testRuntime"("org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}")

            "testImplementation"("org.jetbrains.kotlin:kotlin-test")
            "testImplementation"("io.mockk:mockk:1.10.5")
        }


    }
    val compileTestKotlin by tasks.getting(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }
}
