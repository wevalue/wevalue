package com.wevalue.utils;

import android.app.Activity;

import java.util.Stack;

public class ActivityManagers {

    //	private static final String TAG = "ActivityManagers";
    private static Stack<Activity> activityStack;
    private static ActivityManagers activityManager;
    private String currentAct;

    public void setCurrentAct(String currentAct) {

        this.currentAct = currentAct;
    }

    private ActivityManagers() {

//		LogUtils.i(TAG, "initialization");//初始化
    }

    public static ActivityManagers getActivityManager() {

//		LogUtils.i(TAG, "get activityManager");
        if (activityManager == null) {
            activityManager = new ActivityManagers();
        }
        return activityManager;
    }

    //将当前Activity推入栈中
    public static void pushActivity(Activity activity) {

//		LogUtils.i(TAG, "push an activity");
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //获得当前栈顶Activity
    public Activity currentActivity() {

//		LogUtils.i(TAG, "get current activity");
        Activity activity = null;
        if (!activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    //退出栈顶Activity
    public void popActivity(Activity activity) {

//		LogUtils.i(TAG, "pop activity");
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void removeActivity(Activity activity) {

//		LogUtils.i(TAG, "pop point activity");
        if (activity != null) {
            LogUtils.i(getClass().getName(), activity);
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    //退出栈中所有Activity
    public void popAllActivityException(Class<?> cls) {

//		LogUtils.i(TAG, "pop all point activity class equal" + cls.getName());
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }

            if (activity.getClass().equals(cls)) {
                if (activityStack.size() == 1) {
                    popActivity(activity);
                    break;
                }
            }

            popActivity(activity);
        }
    }

    public void popAllActivities() {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                LogUtils.d("popAllActivities-->", activity.getClass().getName());
                activity.finish();
            }
        }
    }

    //退出其他的activity
    public  void popOtherActivities() {
        if (null != activityStack) {
            for (Activity activity : activityStack) {
                if (!activity.getClass().getName().equals(currentAct)) {
                    LogUtils.d("popAllActivities-->", activity.getClass().getName());
                    activity.finish();
                }
            }
        }
    }

    //退出其他的activity
    public static void exitActivities() {
        if (null != activityStack) {
            for (Activity activity : activityStack) {
                    activity.finish();
            }
        }
    }

    public static boolean isMainActivityExists() {
        if (null != activityStack) {
            for (Activity act : activityStack) {
                if (act.getClass().getName().equals("com.wevalue.MainActivity")) {
                    LogUtils.d("popAllActivities-->", act.getClass().getName());
                    return true;
                }
            }
        }
        return false;
    }
}
