plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.nullandvoid.empowerment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nullandvoid.empowerment"
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
    buildFeatures {
        viewBinding = true
    }
}
configurations.all {
    resolutionStrategy {
        force("com.google.guava:guava:31.1-android")
        force("androidx.concurrent:concurrent-futures:1.1.0")
    }
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation("androidx.concurrent:concurrent-futures:1.1.0")
    implementation("com.google.guava:guava:31.1-android")

    implementation("androidx.concurrent:concurrent-futures:1.1.0")

    implementation(libs.navigation.ui)
    implementation("androidx.databinding:compiler:3.2.0-alpha11")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.github.yalantis:ucrop:2.2.8")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")


}