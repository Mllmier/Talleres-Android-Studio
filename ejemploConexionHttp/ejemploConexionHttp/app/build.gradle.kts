plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "Taller3.guias.ejemploconexionhttp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "Taller3.guias.ejemploconexionhttp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-httpclient:commons-httpclient:3.1")
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-logging:commons-logging:1.2")
}