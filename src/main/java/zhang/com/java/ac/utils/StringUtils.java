package zhang.com.java.ac.utils;

/**
 * Created by win0真垃圾 on 2016/2/12.
 */
public  class StringUtils {
    public  static String handlerDate(String date) {
        StringBuffer s = new StringBuffer(date);
        s.delete(0,2);
       // s.delete(8,11);
        s.replace(8,11," ");
        s.delete(s.lastIndexOf(":"),s.length());

        return s.toString();
    }
    public static String handlerContent(String content) {
        String s = content.replaceAll("<br />", "");

        return s;
    }
}
