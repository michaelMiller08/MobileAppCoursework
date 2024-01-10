
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}



android {
    namespace = "com.example.cafeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cafeapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("de.svenkubiak:jBCrypt:0.4.1")

    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:5.2.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.mockito:mockito-inline:5.2.0")





    // ViewModel and LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation ("com.google.code.gson:gson:2.10")

}
