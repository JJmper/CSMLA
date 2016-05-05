package SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhengheming on 2016/1/22.
 * 创建数据库
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "CSMLA.db";
    private static int DATABASE_VERSION = 1;

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建table




    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table interest(id integer primary key autoincrement,interestid TEXT, interestnumber TEXT,interestname TEXT,interestportrait TEXT,interestsex TEXT,interestcontent TEXT,interesttime TEXT UNIQUE, interestimage TEXT);");
        db.execSQL("create table comment(id integer primary key autoincrement,interest_commentid TEXT UNIQUE,interestid TEXT, interest_comment_active TEXT, interest_comment_passive TEXT,interest_comment_content TEXT,interest_active_name TEXT,interest_passive_name TEXT);");
        db.execSQL("create table user (id integer primary key autoincrement,usernumber TEXT UNIQUE, username TEXT,userportrait TEXT,usersex TEXT, userclassify TEXT,userschool TEXT);");
        db.execSQL("create table words (id integer primary key autoincrement,word TEXT UNIQUE);");
        db.execSQL("create table pedometer (id integer primary key autoincrement,step TEXT,time TEXT UNIQUE);");
        db.execSQL("create table syllabus (id integer primary key autoincrement,name TEXT,place TEXT ,teacher TEXT,start integer,step integer,week integer);");
        db.execSQL("create table attachment (id integer primary key autoincrement,path TEXT UNIQUE,filename TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
