package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import yangchi.cn.myhuanxing.utils.DButils;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.view.ContactView;

/**
 * Created by yangchi on 2018/9/13.
 */
public class ContactPresenterImpl implements ContactsPresenter {

    private ContactView mContactView;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
    }

    private List<String> contactList=new ArrayList<>();

    @Override
    public void initContacts() {
        // 首先走本地缓存,结构化数据(数据库),非结构化数据(图片,电影MP3 SD卡)
        final List<String> contacts = DButils.getContacts(EMClient.getInstance().getCurrentUser());
        contactList.clear();
        contactList.addAll(contacts);
        mContactView.showContacts(contactList);
        // 再走网络
        update();
        // 更新本地缓存

        // 更新界面

    }

    //更新数据
    @Override
    public void upData() {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @Override
    public void delete(final String username) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    //成功
                    mContactView.afterContact(true,username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //失败
                    mContactView.afterContact(false,username);
                }
            }
        });

    }

    private void update() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器得到所有联系人
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //排序，从小到大o1-o2  从大到小o2-o1
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    contactList.clear();
                    contactList.addAll(allContactsFromServer);
                    //更新本地缓存
                    DButils.saveContacts(EMClient.getInstance().getCurrentUser(),contactList);
                    //通知界面更新
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.updateContacts(true);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.updateContacts(false);
                        }
                    });
                }
            }
        });
    }

}
