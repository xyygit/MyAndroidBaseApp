# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lightning/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn okio.**

-keep class * implements android.os.Parcelable {  #保持 Parcelable 不被混淆（aidl文件不能去混淆）
    public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# keep annotated by NotProguard
-keep @cn.trinea.android.common.annotation.NotProguard class * {*;}
-keep class * {
    @cn.trinea.android.common.annotation.NotProguard <fields>;
}
-keepclassmembers class * {
    @cn.trinea.android.common.annotation.NotProguard <methods>;
}

-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void openFileChooser(...);
}