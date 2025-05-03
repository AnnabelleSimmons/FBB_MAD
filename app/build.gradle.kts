plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) version "2.1.0" apply true
    id("com.google.gms.google-services") version "4.4.2" apply true
}

android {
    namespace = "com.example.fbb_mad"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fbb_mad"
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


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("androidx.cardview:cardview:1.0.0")

    //   implementation("com.google.firebase:firebase-auth-ktx:23.2.0")
    //implementation("com.google.firebase:firebase-auth:22.3.0")
//    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")

}