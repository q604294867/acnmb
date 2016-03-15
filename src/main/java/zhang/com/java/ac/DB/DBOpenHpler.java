package zhang.com.java.ac.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by win0真垃圾 on 2016/3/11.
 */
public class DBOpenHpler extends SQLiteOpenHelper {
    public DBOpenHpler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table collect (_id integer primary key autoincrement, chuanID char(10), content char(1024))");
        db.execSQL("create table postLog (_id integer primary key autoincrement, chuanID char(10), content char(1024) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
