plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.txmc.me/releases") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
}

dependencies {
    implementation("me.txmc:protocolapi:1.2-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.4-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.shadowJar {
    exclude("pom.*")
    baseName = "8b8t-PvP"
    minimize()
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