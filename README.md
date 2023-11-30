# Marsad
Marsad is a feature-rich weather app that provides accurate and up-to-date weather information for several locations


## Table of Contents

- [About Marsad](#about-marsad)
- [Features](#features)
- [Screenshots](#screenshots)
- [Technologies and Libraries Used](#technologies-and-libraries-used)
- [Dependencies](#dependencies)
- [Installation](#installation)
- [Contact](#contact)

## About Marsad

## Features
- Get detailed weather information for your current location, including temperature, wind speed, humidity, and more.
- Add locations to your favorites list to get the same weather information for them.
- Schedule weather alerts to get notified when your location has a weather condition of interest.
- Get weather information about your location using GPS or Google Maps.
- Change the app's language to Arabic or English, and switch between temperature and wind speed units.
And much more!

## Screenshots
![Mockup](https://github.com/mahmoodawd/Marsad/assets/63718329/943eb878-2234-4833-b17b-bb54482d20a1)



## Technologies and Libraries Used

- MVVM: Model-View-viewModel architecture pattern
- Repository Pattern: For separating data storage and access from the rest of the app
- Singleton Pattern: To provides a global point of access to certain classes like db and api classes
- Retrofit: For making HTTP requests and handling API responses from [Open Weather](https://openweathermap.org)
- Room: For local data storage and access
- Kotlin coroutines: For reactive programming and asynchronous data flow
- Unit testing with JUnit 4 for view models and repositories
- Local notifications
- Google Material Design
- Alarm Manager and Broadcast Receivers
- Location-based services
  

## Dependencies

Marsad uses the following dependencies:

```groovy
dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    //Coroutines
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.1'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1"
    //Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
    implementation 'com.squareup.moshi:moshi-kotlin:1.14.0'
    //Room
    implementation "androidx.room:room-ktx:2.5.2"
    implementation "androidx.room:room-runtime:2.5.2"
    implementation 'androidx.preference:preference:1.2.0'
    //DataBinding
    implementation 'androidx.databinding:databinding-common:8.0.2'
    kapt "androidx.room:room-compiler:2.5.2"
    //ViewModel
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'
    //Picasso: For network Images
    implementation 'com.squareup.picasso:picasso:2.71828'
    //MDC
    implementation 'com.google.android.material:material:1.9.0'
    //Splash Screen
    implementation 'androidx.core:core-splashscreen:1.0.1'
    //Navigation
    implementation 'androidx.navigation:navigation-fragment:2.5.3'
    implementation 'androidx.navigation:navigation-ui:2.5.3'
    //Location Service
    implementation 'com.google.android.gms:play-services-location:21.0.1'
    //Maps Service
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    //Instrumented Testing
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    /*Unit Testing*/
    //JUnit
    testImplementation 'junit:junit:4.13.2'
    //AndroidX testing
    testImplementation "androidx.test.ext:junit-ktx:1.1.5"
    testImplementation "androidx.test:core-ktx:1.5.0"
    //Robolectric
    testImplementation "org.robolectric:robolectric:4.8"
    //Turbine: For coroutines flows testing
    testImplementation 'app.cash.turbine:turbine:1.0.0'
    //Mockk for creating mock objects
    testImplementation 'io.mockk:mockk:1.12.0'
    //coroutines-test
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1'
    // hamcrest
    testImplementation "org.hamcrest:hamcrest:2.2"
    testImplementation "org.hamcrest:hamcrest-library:2.2"
    // InstantTaskExecutorRule
    testImplementation "androidx.arch.core:core-testing:2.2.0"
}
```
## Installation

To install and run the FoodFlow app, you can follow these steps:

1. Clone the repository to your local machine:
  
   ```
   git clone https://github.com/mahmoodawd/Marsad.git
   ```
2. Open the project in Android Studio.
3. Build the project using the Gradle build system.
4. Run the app on an emulator or a physical device.

That's it! The app should now be up and running on your device.

## Contact
<p align="left"> <a href="https://www.linkedin.com/in/mahmoodawd" target="_blank"><img src="https://img.shields.io/badge/linkedin-%230177B5?style=plastic&logo=linkedin&logoColor=white"/></a> <a href="mailto:mahmooodawd@gmail.com"><img src="https://img.shields.io/badge/gmail-%23FF0000?style=plastic&logo=gmail&logoColor=white"/></a> <a href="https://wa.me/+201141680631" target="_blank"><img src="https://img.shields.io/badge/whatsapp-%25FFA200?style=plastic&logo=whatsapp&logoColor=white"/></a> </p>
 
