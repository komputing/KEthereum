plugins {
  kotlin("kapt")
}

dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    api("com.squareup.moshi:moshi:1.13.0")
    testImplementation(project(":test_data"))
}