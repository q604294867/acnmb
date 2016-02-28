package zhang.com.java.ac.chuan;

import java.util.ArrayList;

/**
 * Created by win0真垃圾 on 2016/2/14.
 */
public class ReplysInfo {
    public String admin;
    public String content;
    public String ext;
    public String id;
    public String img;
    public String name;
    public String now;

    public String sage;
    public String title;
    public String userid;

    public ArrayList<OneReply> replys;

       public  static class OneReply {
        public String admin;
        public String content;
        public String ext;
        public String id;
        public String img;
        public String name;
        public String now;
        public String sage;
        public String title;
        public String userid;
    }

}
