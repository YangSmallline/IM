package yangchi.cn.myhuanxing;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import yangchi.cn.myhuanxing.adapter.TabSelectedListenerAdapter;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.event.ContactUpdateEvent;
import yangchi.cn.myhuanxing.utils.FragmengtFactory;
import yangchi.cn.myhuanxing.view.BaseFragment;

public class MainActivity extends BaseActivity implements EMContactListener {


    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.bngb_main)
    BottomNavigationBar bngbMain;
    private ShapeBadgeItem mItem;
    private TextBadgeItem mBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();
        initFragment();
        initBottonbar();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadMsgCount();
    }


    @Subscribe
    private void initListener() {
        //监听好友通讯录变化的功能

        EventBus.getDefault().register(this);
        EMClient.getInstance().contactManager().setContactListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        //todo 角标
        //更新角标
        updateUnreadMsgCount();
    }

    //更新角标
    private void updateUnreadMsgCount() {
        //获取所有的未读消息
        int count = EMClient.getInstance().chatManager().getUnreadMessageCount();//得到未读消息
        if (count>99){
            mBadgeItem.setText("99+");
            mBadgeItem.show(true);
        }else if(count>0){
            mBadgeItem.setText(count+"");
            mBadgeItem.show(true);
        }else {
            mBadgeItem.hide(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(this);
        EventBus.getDefault().unregister(this);
    }

    private void initBottonbar() {
        mBadgeItem = new TextBadgeItem();
        mBadgeItem.hide()
                .setGravity(Gravity.RIGHT)
                .setBackgroundColorResource(android.R.color.holo_red_dark)
                .setHideOnSelect(false)
                .setAnimationDuration(100)
                .show();

        bngbMain.addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息").setBadgeItem(mBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人"))
                .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态"))
                .setActiveColor(R.color.mainColor)
                .setInActiveColor("#ABADBB")
                .setFirstSelectedPosition(0)//默认选中第一个
                .initialise();

        bngbMain.setTabSelectedListener(new TabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //隐藏其他
                fragmentTransaction.hide(FragmengtFactory.getFragment(0));
                fragmentTransaction.hide(FragmengtFactory.getFragment(1));
                fragmentTransaction.hide(FragmengtFactory.getFragment(2));

                //显示当前
                //判断是否已经添加了，如果添加了，则显示，否则先添加再显示
                BaseFragment fragment = FragmengtFactory.getFragment(position);
                Log.i("tag", "onTabSelected: "+fragment+"position:"+position);
                if (!fragment.isAdded()){
                    fragmentTransaction.add(R.id.fl_content,fragment,position+"");
                }

                fragmentTransaction.show(fragment);

                fragmentTransaction.commit();
            }

        });
    }
    private void initFragment () {
        //默认将第一个显示
        BaseFragment fragment=  FragmengtFactory.getFragment(0);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment,"0").commit();
//        bngbMain.setFirstSelectedPosition(1);
    }

    @Override
    public void onContactAdded(String s) {
        //用eventBus发送时间
        EventBus.getDefault().post(new ContactUpdateEvent(true,s));
    }

    @Override
    public void onContactDeleted(String s) {
        //用eventBus发送时间
        EventBus.getDefault().post(new ContactUpdateEvent(false,s));
    }

    @Override
    public void onContactInvited(String s, String s1) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(s);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFriendRequestAccepted(String s) {

    }

    @Override
    public void onFriendRequestDeclined(String s) {

    }


}