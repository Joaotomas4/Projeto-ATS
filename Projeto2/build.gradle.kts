plugins {
    id("java")
    id("application")
    id("jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "org.spotifumtp37"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://www.evosuite.org/m2") }
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    testImplementation("junit:junit:4.13.2")
    testImplementation(files("evosuite/evosuite-1.2.0.jar"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

application {
    mainClass.set("org.spotifumtp37.Main")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("junit-jupiter", "junit-vintage")
    }
    finalizedBy(tasks.jacocoTestReport)

    jvmArgs(
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.io=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens", "java.desktop/java.awt=ALL-UNNAMED"
    )

    // Excluir testes EvoSuite — incompatíveis com Java 21
    exclude("**/*ESTest*")
}

tasks.register<Test>("testEvoSuite") {
    description = "Corre apenas os testes gerados pelo EvoSuite com Java 8"

    // Usa o Java 8 para correr
    executable = "/usr/lib/jvm/java-8-openjdk-amd64/bin/java"

    useJUnit() // JUnit 4

    // Apenas os ficheiros ESTest
    include("**/*ESTest.class")
    exclude("**/*ESTest_scaffolding.class")

    jvmArgs(
        "-ea",
        "-Djava.awt.headless=true"
    )

    workingDir = projectDir

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}