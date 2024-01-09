plugins {
    id("java")
    id("io.freefair.lombok") version "8.4"
}

group = "de.btu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.commons:commons-configuration2:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:32.0.0-jre")
    implementation("commons-beanutils:commons-beanutils:1.9.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "de.btu.Main"
        attributes["Implementation-Version"] = archiveVersion

        archiveFileName = "SurveySimulator.jar"
    }

    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
    from(dependencies)
}

tasks.withType<JavaCompile> {
    options.release = 8
}