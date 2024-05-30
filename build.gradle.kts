@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	`maven-publish`

	kotlin("jvm") version "1.9.24"
	kotlin("plugin.serialization") version "1.9.24"

	alias(libs.plugins.fabric.loom)

	id("com.modrinth.minotaur") version "2.+"
}

val archivesBaseName: String by project
val minecraftVersion: String by project
base.archivesName.set(archivesBaseName + minecraftVersion)
val version: String by project
group = "amerebagatelle.github.io"

val javaVersion = 21

repositories {
	maven(
		url = "https://maven.terraformersmc.com/"
	)
	maven (
		url = "https://maven.gegy.dev"
	)
}

dependencies {
	//to change the versions see the gradle.properties file
	minecraft(libs.minecraft)
	mappings(
		loom.layered {
			variantOf(libs.yarn) {
				classifier("intermediary-v2")
			}
			officialMojangMappings()
		}
	)
	modImplementation(libs.fabric.loader)

	modImplementation(libs.fabric.api)
	modImplementation(libs.flk)

	modImplementation(libs.modmenu)

	modImplementation(libs.spruceui)
	include(libs.spruceui)

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

	compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			jvmTarget = javaVersion.toString()
			languageVersion = rootProject.libs.plugins.kotlin.get().version.requiredVersion.substringBeforeLast(".")
		}
	}

	withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.isDeprecation = true
		options.isIncremental = true
		options.release.set(javaVersion)
	}

	processResources {
		inputs.property("version", project.version)

		filesMatching("fabric.mod.json") {

		expand(
				mapOf(
					"version" to project.version
				)
			)
		}
	}

	// Change the gradle version here and run `./gradlew wrapper` or `gradle wrapper` to update gradle scripts
	// BIN distribution should be sufficient for the majority of mods
	wrapper {
		distributionType = Wrapper.DistributionType.BIN
	}

	remapJar {
		dependsOn(remapSourcesJar)
	}

	jar {
		from("LICENSE")
	}
}

kotlin {
	jvmToolchain(javaVersion)
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	// If this mod is going to be a library, then it should also generate Javadocs in order to aid with development.
	// Uncomment this line to generate them.
	// withJavadocJar()

	// Still required by IDEs such as Eclipse and VSC
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)
}

val sourceJar = task("sourceJar", Jar::class) {
	dependsOn(tasks["classes"])
	archiveClassifier.set("source")
	from(sourceSets.main.get().allSource)
}

val javadoc = task("javadocJar", Jar::class) {
	archiveClassifier.set("javadoc")
	from(tasks.javadoc)
	from(tasks.javadoc)
}

loom {
	decompilers {
	}
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN"))
	projectId.set("65jTHvHz")
	versionName.set("$archivesBaseName $version")
	versionNumber.set(version)
	versionType.set("release")
	changelog.set(System.getenv("CHANGELOG_BODY"))
	uploadFile.set(tasks.remapJar.get())
	gameVersions.addAll(minecraftVersion)
	loaders.add("quilt")
	dependencies {
		required.project("qsl")
		required.project("qkl")
		optional.project("modmenu")
	}
}
