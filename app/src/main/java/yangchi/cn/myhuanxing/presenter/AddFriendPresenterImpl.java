package yangchi.cn.myhuanxing.presenter;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import yangchi.cn.myhuanxing.MyApplication;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.presenter.AddFriendPresenter;
import yangchi.cn.myhuanxing.utils.DButils;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.utils.ToastUtil;
import yangchi.cn.myhuanxing.view.AddFriendView;

/**
 */
public class AddFriendPresenterImpl implements AddFriendPresenter {

    private AddFriendView mAddFriendView;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        this.mAddFriendView = addFriendView;
    }

    @Override
    public void searchFriend(String keyword) {
        Log.i("yangchi12", "查询联系人方法try: "+keyword);
        //去Bmob服务器中搜索
        BmobQuery<User> query = new BmobQuery<>();
        //只要username中包含keyword的并且不能搜出自己
        query.addWhereContains("username", keyword);
        query.addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    //成功
                    List<String> contacts = DButils.getContacts(EMClient.getInstance().getCurrentUser());

                    mAddFriendView.afterSearch(list, contacts, true);

                    Log.i("yangchi12", "查询联系人方法成功: "+contacts.toString());
                } else {
                    //失败 nodata
                    mAddFriendView.afterSearch(null, null, false);
                    Log.i("yangchi12", "查询联系人方法失败: ");
                }
            }
        });


    }

    @Override
    public void addContact(final String username) {
        Log.i("yangchi", "添加联系人方法addContact: "+username);
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                //添加好友
                try {
                    Log.i("yangchi12", "添加联系人方法try: "+username);
                    EMClient.getInstance().contactManager().addContact(username, "想和你一起玩，赶紧添加我为好友吧。");
                    afterAdd(true, null, username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.i("yangchi12", "添加联系人方法catch: "+username);
                    afterAdd(false, e.getMessage(), username);
                }
            }
        });
    }

    @Override
    public void addalready() {
        mAddFriendView.afteralread("已经添加");
    }

    private void afterAdd(final boolean success, final String msg, final String username) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("yangchi12", "主线程添加联系人方法afterAdd: "+username);
                mAddFriendView.afterAddContact(success, msg, username);
            }
        });
    }

}
