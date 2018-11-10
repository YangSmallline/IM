package yangchi.cn.myhuanxing.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.adapter.ContactAdapter;
import yangchi.cn.myhuanxing.adapter.ConversationAdapter;
import yangchi.cn.myhuanxing.presenter.ConversationPresenter;
import yangchi.cn.myhuanxing.presenter.ConversationPresenterImpl;

/**
 * Created by yangchi on 2018/9/12.
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.OnConversationItemClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.converstationrecycler)
    RecyclerView converstationrecycler;
    Unbinder unbinder;
    private ConversationPresenter mConversationPresenter;
    private ConversationAdapter mConversationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, null);
        unbinder = ButterKnife.bind(this, view);
        mConversationPresenter = new ConversationPresenterImpl(this);
        //开始加载会话数据
        mConversationPresenter.initConversation();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected void initTitle(TextView mTitle) {
        mTitle.setText("消息");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        mConversationPresenter.initConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.initConversation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void initData(List<EMConversation> emConversations) {
        converstationrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ConversationAdapter adapter=new ConversationAdapter(emConversations);
        converstationrecycler.setAdapter(adapter);
        adapter.setOnConversationItemClickListener(this);
    }

    @Override
    public void onItemClick(String username) {
        Intent intent=new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
