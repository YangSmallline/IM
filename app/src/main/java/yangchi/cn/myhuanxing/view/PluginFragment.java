package yangchi.cn.myhuanxing.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import yangchi.cn.myhuanxing.MainActivity;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.presenter.PluginPresenter;
import yangchi.cn.myhuanxing.presenter.PluginPresenterImpl;

/**
 * Created by yangchi on 2018/9/12.
 */
public class PluginFragment extends BaseFragment implements PluginView{
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.btn_loginout)
    Button btnLoginout;

    Unbinder unbinder;

    private PluginPresenter mPluginPresenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, null);
        unbinder = ButterKnife.bind(this, view);
        mPluginPresenter=new PluginPresenterImpl(this);
        String currentUser = EMClient.getInstance().getCurrentUser();
        Log.i("tag", "onCreateView: 当前在线用户为"+currentUser);
        btnLoginout.setText("退出("+currentUser+")");
        return view;
    }

    @Override
    protected void initTitle(TextView mTitle) {
        mTitle.setText("动态");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_loginout)
    public void onViewClicked() {
        mPluginPresenter.logout();
    }

    @Override
    public void afterLogout(boolean isSuccess, String msg) {
        MainActivity activity=(MainActivity)getActivity();
        activity.startActivity(LoginActivity.class,false);
        if (!isSuccess) {
            //getActvitiy与this.StartActivity,如果是返回值,前者返回值给activity,后者返回给fragment
            //为了使用base的方法
            activity.showToast("退出失败");
        }
    }

    //fragment可以启动一个activity,也有onActivityForResult
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
