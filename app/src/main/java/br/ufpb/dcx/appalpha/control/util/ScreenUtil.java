package br.ufpb.dcx.appalpha.control.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Class to lock and unlock touch in screen
 */
public class ScreenUtil {
    private static ScreenUtil instance;

    private ScreenUtil(){    }

    /**
     * Get shared instance
     * @return
     */
    public static synchronized ScreenUtil getInstance(){
        if(instance == null){
            instance = new ScreenUtil();
        }
        return instance;
    }

    /**
     * Lock touch in the activity
     * @param activity
     */
    public void lockScreenTouch(Activity activity){
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Unlock touch in the activity
     * @param activity
     */
    public void unlockScreenTouch(Activity activity){
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
