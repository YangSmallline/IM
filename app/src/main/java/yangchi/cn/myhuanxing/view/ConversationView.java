package yangchi.cn.myhuanxing.view;

import com.hyphenate.chat.EMConversation;

import java.util.List; /**
 * Created by yangchi on 2018/9/17.
 */
public interface ConversationView {

    void initData(List<EMConversation> emConversations);
}
