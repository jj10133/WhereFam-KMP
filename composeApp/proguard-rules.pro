# Keep everything in the Compose runtime
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Koin DI
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Keep Kotlin serialization models
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <fields>;
}
-keepattributes *Annotation*

# Keep RevenueCat
-keep class com.revenuecat.** { *; }
-dontwarn com.revenuecat.**

# Keep Room
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# MapLibre (optional, if needed)
-keep class org.maplibre.** { *; }
-dontwarn org.maplibre.**

# ZXing QR
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# Kermit Logger
-keep class co.touchlab.kermit.** { *; }
-dontwarn co.touchlab.kermit.**

# Add any other libraries or exclusions as needed
