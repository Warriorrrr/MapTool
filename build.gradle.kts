plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply true
}

group = "dev.warriorrr"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("net.sf.jopt-simple:jopt-simple:6.0-alpha-3")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.jline:jline:3.21.0")
    implementation("org.jline:jline-terminal-jansi:3.21.0")
    implementation("com.github.Querz:NBT:6.1")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    shadowJar {
        minimize()
        archiveClassifier.set("")

        dependencies {
            include(dependency("net.sf.jopt-simple:jopt-simple"))
            include(dependency("org.jetbrains:annotations"))
            include(dependency("org.jline:jline"))
            include(dependency("org.jline:jline-terminal-jansi"))
            include(dependency("com.github.Querz:NBT"))
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "dev.warriorrr.maptool.Main"
    }
}
