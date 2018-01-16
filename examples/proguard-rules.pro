-dontwarn sun.reflect.**
-dontwarn sun.misc.**
-dontwarn java.beans.**
-dontwarn sun.nio.ch.**
-dontwarn java.lang.invoke.**
-dontwarn kotlin.internal.jdk8.**
-dontwarn kotlin.internal.jdk7.**
-dontwarn kotlin.internal.**
-dontwarn kotlin.jvm.internal.**
# Only for Perk JVM
-dontwarn COM.newmonics.PercClassLoader.**
# These duplicates can't be avoided
-dontnote android.net.http.**
-dontnote org.apache.http.**

-keep,allowshrinking,includedescriptorclasses class com.esotericsoftware.** {
   <fields>;
   <methods>;
}
-keep,allowshrinking class com.esotericsoftware.kryo.** { *; }
-keep,allowshrinking class com.esotericsoftware.kryo.io.** { *; }
-keep,allowshrinking class org.objenesis.** { *; }
