package SQLite;

import com.jlu.edu.domain.Interest_comment;
import com.jlu.edu.domain.Interest_data;
import com.jlu.edu.domain.UserMsg;
import com.jlu.edu.pedometer.data.bean.PedometerData;
import com.jlu.edu.syllabus.domain.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.ChangeType;
import utils.MyApplication;

/**
 * Created by zhengheming on 2016/1/23.
 */
public class MySQLiteImpl {

    private static MySQLiteInterface mySQLiteInterface = new MySQLiteMethod(MyApplication.getAppContext());

    public boolean addInterestData(int interestid, String interestnumber, String interestname, String interestportrait, String interestsex, String interestcontent, long interesttime, String interestimage) {
        Object[] params = {interestid+"", interestnumber,interestname,interestportrait,interestsex, interestcontent, interesttime+"", interestimage};
        return mySQLiteInterface.addInterestData(params);
    }

    public boolean addInterestComment(String interest_commentid, String interestid, String interest_comment_active, String interest_comment_passive, String interest_comment_content,String interest_active_name,String interest_passive_name) {
        Object[] params = {interest_commentid, interestid, interest_comment_active, interest_comment_passive, interest_comment_content,interest_active_name,interest_passive_name};
        return mySQLiteInterface.addInterestComment(params);
    }

    public boolean addUserData(String usernumber, String username, String userportrait, String usersex, String userclassify, String userschool) {
        Object[] params = {usernumber, username, userportrait, usersex, userclassify, userschool};
        return mySQLiteInterface.addUserData(params);
    }


    public UserMsg receUserData(String usernumber) {
        UserMsg userMsg = new UserMsg();
        String[] params = {usernumber};
        Map<String, String> map = mySQLiteInterface.receUserData(params);
        userMsg.setUserclassify(map.get("userclassify"));
        userMsg.setUsername(map.get("username"));
        userMsg.setUsernumber(map.get("usernumber"));
        userMsg.setUserportrait(map.get("userportrait"));
        userMsg.setUserschool(map.get("userschool"));
        userMsg.setUsersex(map.get("usersex"));
        return userMsg;
    }

    public boolean deleteInterestData() {
        return mySQLiteInterface.deleteInterestData();

    }

    public boolean deleteCommentData() {
        return mySQLiteInterface.deleteInterestComment();
    }

    public List<Interest_data> receInterestData(int start, int end) {
        List<Interest_data> list = new ArrayList<>();
        String[] params = {start + "", end + ""};
        List<Map<String, String>> item = mySQLiteInterface.receInterestData(params);
        int len = item.size();
        for (int i = 0; i < len; i++) {
            Map<String, String> map = item.get(i);
            Interest_data data = new Interest_data();
            data.setInterestid(Integer.valueOf(map.get("interestid")));
            data.setInterestnumber(map.get("interestnumber"));
            data.setInterestname(map.get("interestname"));
            data.setInterestportrait(map.get("interestportrait"));
            data.setInterestsex(map.get("interestsex"));
            data.setInteresttime(Long.valueOf(map.get("interesttime")));
            data.setInterestcontent(map.get("interestcontent"));
            data.setInterestimage(map.get("interestimage"));
            data.setList(receCommentData(data.getInterestid() + ""));
            list.add(data);
        }
        return list;
    }

    public List<Interest_comment> receCommentData(String id) {
        List<Interest_comment> list = new ArrayList<>();
        String[] params = {id};
        List<Map<String, String>> item = mySQLiteInterface.receCommentData(params);
        int len = item.size();
        for (int i = 0; i < len; i++) {
            Map<String, String> map = item.get(i);
            Interest_comment data = new Interest_comment();
            data.setInterestid(Integer.valueOf(map.get("interestid")));
            data.setInterest_commentid(Integer.valueOf(map.get("interest_commentid")));
            data.setInterest_comment_active(map.get("interest_comment_active"));
            data.setInterest_comment_passive(map.get("interest_comment_passive"));
            data.setInterest_comment_content(map.get("interest_comment_content"));
            list.add(data);
        }
        return list;
    }

    public boolean addWord(String word) {
        Object[] params = {word};
        return mySQLiteInterface.addWord(params);
    }

    public boolean queryWord(String word) {
        String[] param = {word};
        return mySQLiteInterface.queryWord(param);
    }

    public boolean deleteWord(String word) {
        Object[] param = {word};
        return mySQLiteInterface.deleteWord(param);
    }

    public List<String> receWords() {

        return mySQLiteInterface.receWords();
    }

    public boolean addPedometerData(int step, String time) {

        Object[] params = {ChangeType.change_I_S(step), time};
        return mySQLiteInterface.addPedometerData(params);
    }

    public boolean updatePedometerData(int step, String time) {
        Object[] params = {ChangeType.change_I_S(step), time};
        return mySQLiteInterface.updatePedometerData(params);
    }

    public boolean addPedometerData(String step, String time) {

        Object[] params = {step, time};
        return mySQLiteInterface.addPedometerData(params);
    }

    public boolean updatePedometerData(String step, String time) {
        Object[] params = {step, time};
        return mySQLiteInterface.updatePedometerData(params);
    }

    public List<PedometerData> recePedometerData() {
        List<PedometerData> pd = new ArrayList<>();
        List<Map<String, String>> list = mySQLiteInterface.recePedometerData();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            PedometerData pedometerData = new PedometerData();
            pedometerData.setStep(list.get(i).get("step"));
            pedometerData.setTime(list.get(i).get("time"));
            pd.add(pedometerData);
        }
        return pd;
    }

    public boolean isPedometerData(String time) {
        String[] param = {time};
        return mySQLiteInterface.isPedometerData(param);
    }

    public Map<String, String> recePedometerData(String time) {
        String[] param = {time};
        return mySQLiteInterface.recePedometerData(param);

    }

    public boolean addSyllabus(Course course) {
        String name = course.getName();
        String place = course.getPlace();
        int start = course.getStart();
        int step = course.getStep();
        String teacher = course.getTeacher();
        int week = course.getWeek();
        Object[] param = {name, place, teacher, start, step, week};
        return mySQLiteInterface.addSyllabus(param);
    }

    public boolean deleteSyllabus(Course course) {
        String name = course.getName();
        String place = course.getPlace();
        int start = course.getStart();
        int step = course.getStep();
        String teacher = course.getTeacher();
        int week = course.getWeek();
        Object[] param = {name, place, teacher, start, step, week};
        return mySQLiteInterface.deleteSyllabus(param);
    }

    public Map<Integer, List<Course>> querySyllabus() {
        return mySQLiteInterface.querySyllabus();
    }

    public List<Course> querySyllabus(int week) {
        String[] param = {ChangeType.change_I_S(week)};
        return mySQLiteInterface.querySyllabus(param);
    }


    public boolean addAttachment(String path, String filename) {
        String[] param = {path, filename};
        return mySQLiteInterface.addAttachment(param);
    }

    public List<Map<String, String>> queryAttachment() {
        return mySQLiteInterface.queryAttachment();
    }

}
