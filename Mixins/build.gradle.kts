
plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
}

dependencies {
    implementation("me.txmc:rtmixin:1.3-BETA")
    compileOnly(project(":Plugin"))
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.shadowJar {
    exclude("pom.*")
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "me.txmc.rtmixin.jagent.AgentMain",
            "Agent-Class" to "me.txmc.rtmixin.jagent.AgentMain",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true",
            "Can-Set-Native-Method-Prefix" to "true"
        )
    }
    minimize()
}

tasks.register("customBuild") {
    project.buildDir.delete()
    dependsOn(tasks.named("shadowJar"))
}

group = "me.sevj6"
version = "1.0-SNAPSHOT"
description = "8b8t pvp plugin"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
//This is to make intelij work properly
tasks.register<Wrapper>("wrapper")
tasks.register("prepareKotlinBuildScriptModel"){}