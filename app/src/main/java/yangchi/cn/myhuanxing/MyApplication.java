package yangchi.cn.myhuanxing;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMClientListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.utils.DButils;
import yangchi.cn.myhuanxing.view.ChatActivity;
import yangchi.cn.myhuanxing.view.LoginActivity;

/**
 * Created by yangchi on 2018/9/11.
 */
public class MyApplication extends Application {
    private List<BaseActivity> mActivityList=new ArrayList<>();
    private ActivityManager mManager;
    private SoundPool mSoundPool;
    private int mDuanId;
    private int mLongId;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMobe();

        initBomb();

        initDB();

        initListener();

        initSoundPool();
    }
    public void addActivity(BaseActivity activity){
        if (!mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }
    public void removeAcitviy(BaseActivity activity){
        mActivityList.remove(activity);
    }


    //音乐池,不能超过1M,加载到内存,所以很快,mediaplay速度慢,游戏几乎所有音效都用这个
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        //给音乐编了个号
        mDuanId = mSoundPool.load(this, R.raw.duan, 1);
        mLongId = mSoundPool.load(this, R.raw.yulu,1);
    }
    //播放音乐

    private boolean IsRuninBackGround() {

        mManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = mManager.getRunningTasks(10);
        ActivityManager.RunningTaskInfo topTask = runningTasks.get(0);
        ComponentName topActivity = topTask.topActivity;
        return !topActivity.equals(getPackageName());
    }


    private void initListener() {
        //添加新的消息
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //发出声音通知,在后台播放长的,前台是短的
                if (IsRuninBackGround()){
                    //0没有声音,1有声音
                    mSoundPool.play(mDuanId,1,1,0,0,1);
                    //显示通知栏
                    showNotification(list.get(0));
                }else{
                    mSoundPool.play(mLongId,1,1,0,0,1);
                }

                //发布事件
                EventBus.getDefault().post(list.get(0));
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }


            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }


        });

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                if (i==EMError.USER_LOGIN_ANOTHER_DEVICE){
                    //将所有activity弄成一个集合

                    for (BaseActivity activity:mActivityList){
                        activity.finish();
                    }
                    mActivityList.clear();
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),"您的账号已经在其他地方登陆了",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showNotification(EMMessage emMessage) {
        if (mNotificationManager==null){
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        EMMessageBody body=emMessage.getBody();
        String msg="";
        if (body instanceof EMTextMessageBody){
            msg=((EMTextMessageBody) body).getMessage();
        }

//        Context baseContext = getBaseContext();
        //setPriority

        //防止打开对话页面后退出后到了桌面
        Intent mainIntent=new Intent(this,MainActivity.class);
        //可以开启一个acitiviy,但是得加个newtask
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//singleTask


        Intent chatIntent=new Intent(this,ChatActivity.class);
        chatIntent.putExtra("username",emMessage.getUserName());
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//singleTask


        Intent [] intents=new Intent[]{mainIntent,chatIntent};

        PendingIntent pendingIntent=PendingIntent.getActivities(this,0,intents,PendingIntent.FLAG_UPDATE_CURRENT);//让通知栏自动更新

        Notification notification=new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.avatar3))
                .setSmallIcon(R.mipmap.contact_selected_2)//不需要
                .setContentTitle(emMessage.getUserName())
                .setContentText(msg)
                .setContentInfo("来自环信")
                .setPriority(Notification.PRIORITY_MAX)//5.锁屏界面也显示
                .setAutoCancel(true)
//                .setColor()//给小图标设置颜色,版本2.5.6在大图片的右下角
//                .setCustomContentView()   //自定义通知栏
                .setContentIntent(pendingIntent)
                .build();

        mNotificationManager.notify(1,notification);

    }

    private void initDB() {
        DButils.init(this);
    }

    private void initBomb() {
        //第一：默认初始化
        Bmob.initialize(this, "377c242ec4c7f3f023a46e189f7448e7");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);

    }

    private void initEaseMobe() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoAcceptGroupInvitation(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAcceptInvitationAlways(true);


        //避免多次初始化
        int pid = Process.myPid();

        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getApplicationContext().getPackageName())) {
            Log.e("tag", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }


//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
