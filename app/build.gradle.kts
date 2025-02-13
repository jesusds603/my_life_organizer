plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.mylifeorganizer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mylifeorganizer"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Para el sistema
    implementation(libs.accompanist.systemuicontroller)

    // Para un mejor fluje de Rows permitiendo que el texto se acomode en lineas
    // implementation("com.google.accompanist:accompanist-flowlayout:0.36.0")

    // Para markdown y latex
    implementation(libs.core)
    // implementation("io.github.kexanie:MathView:0.5.0")
    implementation(libs.accompanist.webview)

    // Para manejar la cincompatibilidad de java.time
//    implementation("org.threeten:threetenbp:1.6.0")
//    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    //implementation(libs.androidx.work.runtime.ktx) // Para WorkManager
    //implementation(libs.androidx.core.ktx) // Para NotificationCompat

    implementation(libs.gson)

    // Para gráficos
    implementation(libs.mpandroidchart)
}