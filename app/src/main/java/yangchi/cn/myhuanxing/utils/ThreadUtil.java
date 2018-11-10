package yangchi.cn.myhuanxing.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.exceptions.Exceptions;

/**
 * Created by yangchi on 2018/9/12.
 */
public class ThreadUtil {

    private static Executor execute= Executors.newCachedThreadPool();

    private static Handler handler=new Handler(Looper.getMainLooper());//获得主线程的


    public static void runOnSubThread(Runnable runnable){
        execute.execute(runnable);

    }

    public static void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }
}
