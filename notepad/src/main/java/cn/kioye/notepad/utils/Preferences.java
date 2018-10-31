package cn.kioye.notepad.utils;

public class Preferences extends DrawUtil {

    private static final String Prefrence = "notepad-preferences";

    private static final java.util.prefs.Preferences preferences=java.util.prefs.Preferences.userRoot().node(Prefrence);

    public static java.util.prefs.Preferences getPreferences() {
        return preferences;
    }
    public static String path=System.getProperty("user.dir")+"/dictionarys.txt";

    public static String getPath() {
        return preferences.get("path", path);
    }
    public static void setPath(String path) {
        preferences.put("path", path);
    }
}
