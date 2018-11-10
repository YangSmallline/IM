package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import yangchi.cn.myhuanxing.adapter.EMCallBackAdapter;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.view.ChatView;

/**
 * Created by yangchi on 2018/9/16.
 */
public class ChatPresentImpl implements ChatPresenter {
    ChatView mChatView;
    private List<EMMessage> mEMMessages=new ArrayList<>();
    public ChatPresentImpl(ChatView chatView) {
        mChatView = chatView;
    }

    @Override
    public void sendMessage(final EMMessage msg) {
        //先添加到集合中,然后让adapter刷新一下
        msg.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessages.add(msg);
        afterSend(true,null,msg);
        //通过环信的sdk发送出去
        msg.setMessageStatusCallback(new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                //成功
                afterSend(true,null,msg);
            }

            @Override
            public void onError(int i,final String s) {
                //失败
                afterSend(true,s,msg);
            }
        });
        //从网络发送出去
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().chatManager().sendMessage(msg);//报异常一般是异步的
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //发送的时候监听发送的状态

        //再notifit一下
    }

    private void afterSend(final boolean success, final String msg, final EMMessage message){
        //通知
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatView.notifyData( success, msg, message);
            }
        });

    }

    @Override
    public void initChatData(String username,boolean isSmooth) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
//        List<EMMessage> allMessages = conversation.getAllMessages();//从内存拿到一条数据
//        conversation.loadMoreMsgFromDB()//拿到全部的数据
        if (conversation==null){
            //第一次聊天
            mEMMessages.clear();
            mChatView.afterInitData(mEMMessages,false);

        }else {
            //已读
            conversation.markAllMessagesAsRead();

            int allMsgCount = conversation.getAllMsgCount();//统计数据库中我和username所有的聊天个数
            //需要获取最近的一条消息

            allMsgCount=allMsgCount>19?19:allMsgCount;

            EMMessage lastMessage = conversation.getLastMessage();
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), allMsgCount);

            //先清除
            mEMMessages.clear();
            mEMMessages.addAll(emMessages);
            mEMMessages.add(lastMessage);
            mChatView.afterInitData(mEMMessages,isSmooth);
        }
    }
}
