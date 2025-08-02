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