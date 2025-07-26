-keepclassmembers class com.google.firebase.database.GenericTypeIndicator{*;}
-keep class * extends com.google.firebase.database.GenericTypeIndicator{*;}
-keep class com.google.firebase.database.GenericTypeIndicator{*;}

-keepclassmembers class * {
    public <init>();
}

-keepclassmembers class **$* {
    public <init>();
}

-keep class **.data.** {
    *;
}

-keepclassmembers class **.data.** {
    public <init>(...);
}