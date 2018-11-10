package yangchi.cn.myhuanxing.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.presenter.RegistPresenter;
import yangchi.cn.myhuanxing.presenter.RegisterPresenterImpl;
import yangchi.cn.myhuanxing.utils.StringUtils;

/**
 * Created by yangchi on 2018/9/12.
 */
public class RegistActivity extends BaseActivity implements RegistVIew, TextView.OnEditorActionListener {

    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    private RegistPresenter mRegistPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        etPwd.setOnEditorActionListener(this);
        mRegistPresenter = new RegisterPresenterImpl(this);
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            regist();
            return true;
        }
        return false;
    }

    private void regist() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (StringUtils.checkUsername(username)) {
            showToast("用户不合法");
        } else if (StringUtils.checkPwd(pwd)) {
            showToast("密码不合法");
        }

        mRegistPresenter.regist(username, pwd);

    }

    @Override
    public void showProgressDialog(String msg) {
        showDialog(msg, true);//www.stackOverflow.com
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }

    @Override
    public void afterRegist(User user, boolean b, String msg) {
        if (b) {
            //sp保存数据
            //跳转到登录界面
            saveUser(user);
            startActivity(LoginActivity.class, true);
        } else {
            //弹吐司
            showToast(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegistPresenter = null;
    }

    @OnClick(R.id.btn_regist)
    public void onViewClicked() {
        regist();
    }
}
