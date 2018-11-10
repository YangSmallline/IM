package yangchi.cn.myhuanxing.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import yangchi.cn.myhuanxing.MainActivity;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.adapter.ContactAdapter;
import yangchi.cn.myhuanxing.event.ContactUpdateEvent;
import yangchi.cn.myhuanxing.presenter.ContactPresenterImpl;
import yangchi.cn.myhuanxing.presenter.ContactsPresenter;
import yangchi.cn.myhuanxing.utils.ToastUtil;
import yangchi.cn.myhuanxing.widget.ContactRecycleView;

/**
 * Created by yangchi on 2018/9/12.
 */
public class ContactsFragment extends BaseFragment implements ContactView, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.OnItemClickListener {


    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.contact_recyclerview)
    ContactRecycleView contactRecyclerview;

    Unbinder unbinder;

    private ContactAdapter mContactAdapter;

    private ContactsPresenter mContactsPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        unbinder = ButterKnife.bind(this, view);
        mContactsPresenter = new ContactPresenterImpl(this);
        ivRight.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化联系人
        mContactsPresenter.initContacts();

        contactRecyclerview.setOnRefreshListener(this);


    }

    @Override
    protected void initTitle(TextView mTitle) {
        mTitle.setText("联系人");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactUpdateEvent contactUpdateEvent) {
        mContactsPresenter.initContacts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showContacts(List<String> contacts) {
        //这个走的是本地数据库
        mContactAdapter = new ContactAdapter(contacts);
        contactRecyclerview.setAdapter(mContactAdapter);
        mContactAdapter.setOnItemClickListener(this);
    }

    @Override
    public void updateContacts(boolean issuccess) {
        contactRecyclerview.setRefreshing(false);//隐藏
        if (issuccess) {
            mContactAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast(getActivity(), "通讯录同步失败");
        }
    }

    @Override
    public void afterContact(boolean isSuccess, String username) {
        //成功
        if (isSuccess){
            ToastUtil.showToast(getActivity(),"删除成功");
        }else{
            ToastUtil.showToast(getActivity(),"删除失败:"+username);
        }
    }

    @Override
    public void onRefresh() {
        //更新数据
        mContactsPresenter.upData();
    }

    @OnClick(R.id.iv_right)
    public void onViewClicked() {
        //跳转到添加好友界面
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(AddFriendActivity.class, false);
    }


    @Override
    public void onItemClick(String username) {
        //点击事件跳转界面
        Intent intent=new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        MainActivity activity= (MainActivity) getActivity();
        activity.startActivity(intent);
    }

    @Override
    public void onItemLongClick(final String username) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage("您确定和" + username + "取消好友关系吗")
                .setPositiveButton("友尽", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContactsPresenter.delete(username);
                    }
                })
                .setPositiveButton("再续前缘", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }
}
