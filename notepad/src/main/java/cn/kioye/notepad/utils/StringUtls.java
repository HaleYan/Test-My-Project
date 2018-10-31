package cn.kioye.notepad.utils;

public class StringUtls extends TaskWithLoading {
    private StringUtls(){

    }
    /**
     * 大写首字母
     * @param str
     * @return
     */
    public static String UpFirstCase(String str){
        char[] charArray = str.toCharArray();
        // a-z 为97-122
        if (charArray[0]>96&&charArray[0]<123)
            charArray[0]-=32;
        return String.valueOf(charArray);
    }
}
