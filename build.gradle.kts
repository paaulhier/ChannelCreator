plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.keeeks.channelcreator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.jline:jline:3.21.0")
    implementation("org.jline:jline-terminal-jansi:3.21.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.github.theholywaffle:teamspeak3-api:1.3.0")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "de.keeeks.channelcreator.Bootstrap"
    }
}