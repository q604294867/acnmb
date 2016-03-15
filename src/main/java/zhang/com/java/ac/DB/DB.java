package zhang.com.java.ac.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import zhang.com.java.ac.bean.DBChuanInfo;
import zhang.com.java.ac.bean.DBReplyInfo;

/**
 * Created by win0真垃圾 on 2016/3/11.
 */
public class DB {
    private DBOpenHpler dbOpenHpler ;
    public DB (Context context) {
        dbOpenHpler = new DBOpenHpler(context, "Chuan.db", null, 1);
    }

    public boolean add (String tableNmae ,String id,String content) {
        SQLiteDatabase db = dbOpenHpler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("chuanID",id);
        values.put("content", content);
        long rowID = db.insert(tableNmae, null, values);
        if (rowID==-1) {
            return false;
        }else {
            return true;
        }
    }
    public boolean delete (String tableName,String id) {
          SQLiteDatabase db = dbOpenHpler.getWritableDatabase();
          int rowID = db.delete(tableName, "chuanID = ?", new String[]{id});
          if (rowID==0) {
              return false;
          }else {
              return true;
          }
      }
    public ArrayList query (String tableNmae) {
        SQLiteDatabase db = dbOpenHpler.getWritableDatabase();
        Cursor cursor = db.query(tableNmae, new String[]{"chuanID", "content"}, null, null, null, null, null);
        ArrayList list = new ArrayList();
        if (tableNmae.equals("collect")) {
            while (cursor.moveToNext()) {
                DBChuanInfo chuanInfo = new DBChuanInfo();
                chuanInfo.id = cursor.getString(0);
                chuanInfo.content = cursor.getString(1);
                list.add(chuanInfo);
            }
        }else {
            while (cursor.moveToNext()) {
                DBReplyInfo replyInfo = new DBReplyInfo();
                replyInfo.id = cursor.getString(0);
                replyInfo.content = cursor.getString(1);
                list.add(replyInfo);
            }
        }
        return list;
    }
}
