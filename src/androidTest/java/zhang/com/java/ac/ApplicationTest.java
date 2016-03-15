package zhang.com.java.ac;

import android.app.Application;
import android.test.ApplicationTestCase;

import zhang.com.java.ac.DB.DB;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testAdd() {
        DB db = new DB(getContext());
        db.add("collect", "111", "哈士大夫似的");
    }
    public void testDelete() {
        DB db = new DB(getContext());
        db.delete("collect","111");
    }
}