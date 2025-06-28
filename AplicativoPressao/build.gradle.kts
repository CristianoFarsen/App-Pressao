// build.gradle.kts (do projeto raiz, APÓS a correção)

plugins {
    id("com.android.application") version "8.3.0" apply false // Mantenha sua versão aqui
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false // Mantenha sua versão aqui
}

// Este bloco 'allprojects' e 'repositories' DEVE SER REMOVIDO daqui.
// Ele será movido para settings.gradle.kts.

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}