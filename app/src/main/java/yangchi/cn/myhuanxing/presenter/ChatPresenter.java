package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by yangchi on 2018/9/16.
 */
public interface ChatPresenter {

    void sendMessage(EMMessage msg);


    void initChatData(String username, boolean b);
}
