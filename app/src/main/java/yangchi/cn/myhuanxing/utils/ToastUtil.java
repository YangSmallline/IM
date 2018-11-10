package yangchi.cn.myhuanxing.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import yangchi.cn.myhuanxing.MyApplication;

/**
 * Created by yangchi on 2018/9/11.
 */
public class ToastUtil {

    private static Toast stoast;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(Context context, String msg) {
        if (stoast == null) {
            stoast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }

        //判断当前线程是否是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            stoast.show();
        }else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    stoast.show();
                }
            });
        }


    }
}
