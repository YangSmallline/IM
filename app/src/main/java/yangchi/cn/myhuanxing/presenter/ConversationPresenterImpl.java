package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import yangchi.cn.myhuanxing.view.ConversationView;

/**
 * Created by yangchi on 2018/9/17.
 */
public class ConversationPresenterImpl implements ConversationPresenter {
    private ConversationView mConversationView;
    private List<EMConversation> mEMConversations = new ArrayList<>();

    public ConversationPresenterImpl(ConversationView conversationView) {
        mConversationView = conversationView;
    }


    @Override
    public void initConversation() {
        //获取所有的会话
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        mEMConversations.clear();
        mEMConversations.addAll(allConversations.values());
        //从前往后排,从后往前排
        Collections.sort(mEMConversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        mConversationView.initData(mEMConversations);
    }
}
