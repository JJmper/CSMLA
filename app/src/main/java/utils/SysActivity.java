package utils;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengheming on 2016/4/22.
 */
public class SysActivity {
    private Map<String, Activity> map = new HashMap<>();
    private static SysActivity instance;

    private SysActivity() {
    }

    public synchronized static SysActivity getInstance() {
        if (null == instance) {
            instance = new SysActivity();
        }
        return instance;
    }

    public void addActivity(String activityname, Activity activity) {
        map.put(activityname, activity);
    }

    public void exit(String... key) {
        try {
            for (String subkey : key) {
                Activity activity = map.get(subkey);
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
