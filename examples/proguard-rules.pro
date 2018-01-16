-dontwarn sun.reflect.**
-dontwarn sun.misc.**
-dontwarn java.beans.**
-dontwarn sun.nio.ch.**
-dontwarn java.lang.invoke.**
# These are not used at runtime actually
-dontnote sun.reflect.**
-dontnote kotlin.internal.jdk8.**
-dontnote kotlin.internal.jdk7.**
-dontnote kotlin.internal.**
-dontnote kotlin.jvm.internal.**
# Only for Perk JVM
-dontnote COM.newmonics.PercClassLoader.**
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
