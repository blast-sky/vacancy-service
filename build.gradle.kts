import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.unbroken-dome.test-sets") version "4.0.0" // for integration tests source
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("kapt") version "1.8.22"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.astrog"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

springBoot {
	buildInfo()
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}


testSets {
	create("integrationTest")
}

val integrationTestImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.github.oshai:kotlin-logging-jvm:4.0.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	kapt("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("io.projectreactor:reactor-test")

	integrationTestImplementation("io.projectreactor:reactor-test")
	integrationTestImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.mockito", module = "mockito-core")
	}
	integrationTestImplementation("org.springframework.amqp:spring-rabbit-test")
	integrationTestImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
