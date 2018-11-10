package yangchi.cn.myhuanxing.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.adapter.SearchAdapter;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.presenter.AddFriendPresenter;
import yangchi.cn.myhuanxing.presenter.AddFriendPresenterImpl;
import yangchi.cn.myhuanxing.utils.ToastUtil;

public class AddFriendActivity extends BaseActivity implements TextView.OnEditorActionListener, AddFriendView, SearchAdapter.OnAddFriendCLickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView mRecyclerView;
    @BindView(R.id.iv_sarch)
    ImageView ivSarch;
    @BindView(R.id.iv_nodata)
    ImageView ivNodata;

    private AddFriendPresenter mAddFriendPresenter;
    private InputMethodManager mInputmessager;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        etUsername.setOnEditorActionListener(this);
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        ivRight.setVisibility(View.VISIBLE);
        mInputmessager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ivSarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                search();
            }
        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }


    public void search() {
        String username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showToast("你让我搜个鬼");
            return;
        } else {
            mAddFriendPresenter.searchFriend(username);
            if (mInputmessager.isActive()){
                //切换
                mInputmessager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void afterSearch(List<User> usernames, List<String> contact, boolean isSuccess) {
        //如果成功查询
        if (isSuccess) {
            ivNodata.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new SearchAdapter(usernames,contact);
            mRecyclerView.setAdapter(mAdapter);
            showToast("搜索结束");
            mAdapter.setOnAddFriendClickListener(this);
        } else {
            ivNodata.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            showToast("没有搜索到结果");
        }
    }

    @Override
    public void afterAddContact(boolean isSuccess, String msg, String username) {
        showToast("得到了");
        if (isSuccess){
            ToastUtil.showToast(this,"发送请求成功");
            Log.i("yangchi12", "发送请求成功:afterAddContact "+username);
        }else{
            showToast("发送请求失败");
            Log.i("yangchi12", "发送请求失败:afterAddContact "+username);
        }
    }

    @Override
    public void afteralread(String name) {
        showToast(name);
    }

    @Override
    public void onClick(String username) {
        mAddFriendPresenter.addContact(username);
        Log.i("yangchi12", "点击事件:onClick: "+username);
        if (username.equals("已是好友")){
            mAddFriendPresenter.addalready();
        }
    }
}
