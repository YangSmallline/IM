package yangchi.cn.myhuanxing.common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import yangchi.cn.myhuanxing.MyApplication;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.utils.ToastUtil;

/**
 * Created by yangchi on 2018/9/11.
 */
public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler= new Handler();
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private static final String USERNAME_KEY="username";
    private static final String PSW_KEY="psw";
    private MyApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一个acitvity有windows对象
        //2.0GC算法可以回收两个互相引用
        //day01,内存泄漏的原因n种
        //内存溢出分为栈溢出和堆溢出
        mProgressDialog = new ProgressDialog(this);
        mSharedPreferences = getSharedPreferences("conifg", MODE_PRIVATE);

        mApplication = (MyApplication) getApplication();
        mApplication.addActivity(this);
    }

    @SuppressLint("ApplySharedPref")
    public void saveUser(User user){
        mSharedPreferences.edit().
                putString(USERNAME_KEY,user.getUsername())
                .putString(PSW_KEY,user.getPassword2()).commit();
    }

    public User getUser(){
        String username = mSharedPreferences.getString(USERNAME_KEY, null);
        String pwd=mSharedPreferences.getString(PSW_KEY,"");
        User user=new User(username,pwd);
        return user;
    }

    public void showDialog(String msg,boolean is_Canclable){
        mProgressDialog.setCancelable(is_Canclable);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideDialog(){
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
        mApplication.removeAcitviy(this);
    }

    //跳转
    public void startActivity(Class clazz,boolean isFinish){
        startActivity(new Intent(this,clazz));
        if (isFinish){
            finish();
        }
    }

    public void showToast(String msg){
        ToastUtil.showToast(this,msg);
    }
}
