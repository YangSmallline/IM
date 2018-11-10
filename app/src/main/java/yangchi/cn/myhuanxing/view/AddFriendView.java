package yangchi.cn.myhuanxing.view;

import java.util.List;

import yangchi.cn.myhuanxing.model.User;

/**
 * Created by yangchi on 2018/9/14.
 */
public interface AddFriendView {
    /**
     * @param usernames 从bmob服务器上搜索出来的用户
     * @param contact 当前用户的好友
     * @param isSuccess
     */
    void afterSearch(List<User> usernames, List<String> contact, boolean isSuccess);

    void afterAddContact(boolean isSuccess,String msg,String username);

    void afteralread(String name);
}
