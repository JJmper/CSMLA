package SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jlu.edu.syllabus.domain.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengheming on 2016/1/22.
 */
public class MySQLiteMethod implements MySQLiteInterface {
    private static MySQLiteOpenHelper mySQLiteOpenHelper;

    public MySQLiteMethod(Context context) {
        mySQLiteOpenHelper = getInstance(context);
    }

    //单例模式
    private synchronized static MySQLiteOpenHelper getInstance(Context context) {
            if (mySQLiteOpenHelper == null) {
                mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
            }
        return mySQLiteOpenHelper;
    }

    //添加兴趣圈信息
    @Override
    public boolean addInterestData(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert into interest ( interestid, interestnumber,interestname,interestportrait,interestsex,interestcontent,interesttime, interestimage ) values(?,?,?,?,?,?,?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    //添加评论信息
    @Override
    public boolean addInterestComment(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert into comment ( interest_commentid ,interestid , interest_comment_active, interest_comment_passive,interest_comment_conten,interest_active_name,interest_passive_name) values(?,?,?,?,?,?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return flag;
    }

    //添加用户信息
    @Override
    public boolean addUserData(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert into user ( usernumber , username ,userportrait ,usersex, userclassify,userschool ) values(?,?,?,?,?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return flag;
    }

    @Override
    public boolean deleteInterestData() {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "delete from interest ";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Object[] params={};
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public boolean deleteInterestComment() {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "delete from comment";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Object[] params={};
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public Map<String, String> receUserData(String[] params) {
        Map<String, String> map = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from " + "user" + " where usernumber=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, params);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
            cursor.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return map;
    }

    @Override
    public List<Map<String, String>> receInterestData(String[] selectionArgs) {

        List<Map<String, String>> list = new ArrayList<>();
        String sql = "select * from interest order by interesttime desc;";
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                cursor.close();
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return list;
    }

    @Override
    public List<Map<String, String>> receCommentData(String[] selectionArgs) {

        List<Map<String, String>> list = new ArrayList<>();
        String sql = "select * from comment where interestid=? order by interest_commentid ;";
        SQLiteDatabase sqLiteDatabase = null;
        try {

            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                cursor.close();
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return list;
    }

    @Override
    public boolean addWord(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert  into words (word) values(?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public boolean queryWord(String[] params) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from " + "words" + " where word=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, params);
            int len = cursor.getCount();
            cursor.close();
            if (len != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return false;
    }

    @Override
    public boolean deleteWord(Object[] params) {

        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "delete from words where word=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public List<String> receWords() {

        List<String> list = new ArrayList<>();
        String sql = "select * from words order by word;";
        SQLiteDatabase sqLiteDatabase = null;
        try {

            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                String cols_name = cursor.getColumnName(1);
                String cols_value = cursor.getString(cursor
                        .getColumnIndex(cols_name));
                if (cols_value == null) {
                    cols_value = "";
                }
                cursor.close();
                list.add(cols_value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return list;
    }

    @Override
    public boolean addPedometerData(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert  into pedometer (step,time) values(?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updatePedometerData(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "update pedometer set step=? where time=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public List<Map<String, String>> recePedometerData() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "select * from pedometer order by time desc ;";
        SQLiteDatabase sqLiteDatabase = null;
        try {

            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                cursor.close();
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }
        return list;
    }

    @Override
    public boolean isPedometerData(String[] params) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from " + "pedometer" + " where time=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, params);
            int len = cursor.getCount();
            if (len != 0) {
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return false;
    }


    @Override
    public Map<String, String> recePedometerData(String[] params) {
        Map<String, String> map = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from pedometer  where time=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, params);
            int colums = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < colums; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
            cursor.close();
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return map;
    }


    @Override
    public boolean addSyllabus(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert into syllabus (name,place,teacher,start,step,week) values(?,?,?,?,?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public boolean deleteSyllabus(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "delete from syllabus where name=?&&place=?&&teacher=?&&start=?&&step=?&&week=?";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }

    @Override
    public Map<Integer, List<Course>> querySyllabus() {
        Map<Integer, List<Course>> map = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from syllabus order by week";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            int week_temp=0 ;
            int temp = 0;
            int temp_sum=0;
            List<Course> list = null;

            while (cursor.moveToNext()) {
                int week = cursor.getInt(cursor.getColumnIndex("week"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String place=cursor.getString(cursor.getColumnIndex("place"));
                String teacher=cursor.getString(cursor.getColumnIndex("teacher"));
                int start=cursor.getInt(cursor.getColumnIndex("start"));
                int step=cursor.getInt(cursor.getColumnIndex("step"));
                if(temp_sum==0){
                    week_temp=week;
                    temp_sum++;
                }

                if (week_temp != week) {
                    map.put(week_temp, list);
                    list = null;
                    temp = 0;
                    week_temp = week;
                }
                if (temp == 0) {
                    list = new ArrayList<>();
                    temp++;
                }
                Course course = new Course();
                course.setName(name);
                course.setPlace(place);
                course.setTeacher(teacher);
                course.setStart(start);
                course.setStep(step);
                course.setWeek(week);
                list.add(course);


            }
            map.put(week_temp, list);
            cursor.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return map;
    }

    @Override
    public List<Course> querySyllabus(String[] params) {
        List<Course> list=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from syllabus where week=? order by start";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, params);
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.setName(cursor.getString(cursor.getColumnIndex("name")));
                course.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                course.setStart(cursor.getInt(cursor.getColumnIndex("start")));
                course.setStep(cursor.getInt(cursor.getColumnIndex("step")));
                course.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
                list.add(course);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        return list;
    }

    @Override
    public boolean addAttachment(Object[] params) {
        boolean flag = false;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "insert into attachment (path,filename) values(?,?)";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return flag;
    }
    @Override
    public List<Map<String, String>> queryAttachment() {
        List<Map<String, String>> list=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = "select * from attachment";
            sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Map<String,String> map=new HashMap<>();
                map.put("path", cursor.getString(cursor.getColumnIndex("path")));
                map.put("filename", cursor.getString(cursor.getColumnIndex("filename")));
                list.add(map);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }

        }

        return list;
    }
}