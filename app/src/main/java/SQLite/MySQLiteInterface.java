package SQLite;

import com.jlu.edu.syllabus.domain.Course;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengheming on 2016/1/23.
 */
public interface MySQLiteInterface {
    //添加兴趣圈信息
     boolean addInterestData(Object[] params);

     boolean addInterestComment(Object[] params);

     boolean addUserData(Object[] params);

     boolean deleteInterestData();
     boolean deleteInterestComment();
    //取出信息
     Map<String, String> receUserData(String[] number);

     List<Map<String, String>> receInterestData(String[] selectionArgs);

     List<Map<String, String>> receCommentData(String[] selectionArgs);

    //生词本
     boolean addWord(Object[] params);

     boolean queryWord(String[] params);

     boolean deleteWord(Object[] params);

     List<String> receWords();

    //计时器
     boolean addPedometerData(Object[] params);

     boolean updatePedometerData(Object[] params);

     List<Map<String, String>> recePedometerData();

     Map<String, String> recePedometerData(String[] params);

     boolean isPedometerData(String[] params);

    //课程表
     boolean addSyllabus(Object[] params);
     boolean deleteSyllabus(Object[] params);
     Map<Integer,List<Course>> querySyllabus();
     List<Course> querySyllabus(String[] params);

    //附件路径

    boolean addAttachment(Object[] params);
    List<Map<String,String>> queryAttachment();
}
