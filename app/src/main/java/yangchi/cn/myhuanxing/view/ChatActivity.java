package yangchi.cn.myhuanxing.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.adapter.ChatAdapter;
import yangchi.cn.myhuanxing.adapter.TextWatcharAdapter;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.presenter.ChatPresentImpl;
import yangchi.cn.myhuanxing.presenter.ChatPresenter;

public class ChatActivity extends BaseActivity implements TextView.OnEditorActionListener,ChatView   {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.charRecyclerView)
    RecyclerView charRecyclerView;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.btn_send)
    Button btnSend;

    private ChatPresenter mChatPresenter;
    private String mUsername;
    private ChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        ivLeft.setVisibility(View.VISIBLE);
        setTitle();
        initTextWatch();
        mChatPresenter=new ChatPresentImpl(this);
        mChatPresenter.initChatData(mUsername,false);
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //判断当前接收到的消息是否发送给当前聊天
        if (mUsername.equals(emMessage.getUserName())){
            //更新recycler
            mChatPresenter.initChatData(mUsername,true);
        }
    }

    private void initTextWatch() {
        etMsg.setOnEditorActionListener(this);
        etMsg.addTextChangedListener(new TextWatcharAdapter(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.toString().trim().length()==0){
                    btnSend.setEnabled(false);
                }else{
                    btnSend.setEnabled(true);
                }
            }
        });
    }

    public void setTitle() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        if (TextUtils.isEmpty(mUsername)) {
            showToast("数据没有传过来");
        }
        tvTitle.setText(mUsername);
    }

    @OnClick({R.id.iv_left, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND){
            sendMsg();
        }
        return true;
    }

    private void sendMsg() {
        String msg = etMsg.getText().toString();
        if (TextUtils.isEmpty(msg)){
            showToast("消息不能为空");
            return;
        }
        etMsg.getText().clear();//清除编辑框的文本
        EMMessage emMessage=EMMessage.createTxtSendMessage(msg,mUsername);
        mChatPresenter.sendMessage(emMessage);
    }


    @Override
    public void afterInitData(List<EMMessage> emMessages,boolean isSmooth) {
        //接收到消息
        if (mAdapter==null){
            charRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new ChatAdapter(emMessages);
            charRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }

        if (isSmooth){
            charRecyclerView.smoothScrollToPosition(emMessages.size()-1);
        }else{
            charRecyclerView.scrollToPosition(emMessages.size()-1);
        }

        //已读
    }

    @Override
    public void notifyData(boolean success, String msg, EMMessage message) {
        mAdapter.notifyDataSetChanged();
        //一刷新就自动滚动
        charRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE);//平滑滚动
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
