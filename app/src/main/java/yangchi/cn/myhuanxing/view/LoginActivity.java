package yangchi.cn.myhuanxing.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import java.security.Permission;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yangchi.cn.myhuanxing.MainActivity;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.presenter.LoginPresenter;
import yangchi.cn.myhuanxing.presenter.LoginPresenterImpl;
import yangchi.cn.myhuanxing.utils.StringUtils;

public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_newuser)
    TextView tvNewuser;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = new LoginPresenterImpl(this);
        etPwd.setOnEditorActionListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //数据回显
        User user = getUser();
        etUsername.setText(user.getUsername());
        etPwd.setText(user.getPassword2());
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
//                startActivity(MainActivity.class,true);
                break;
            case R.id.tv_newuser:
                showToast("跳转到注册界面");
                startActivity(RegistActivity.class,false);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            login();
            return true;
        }
        return false;
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();

//        if (!StringUtils.checkUserName(username)) {
//            showToast("用户名不合法");
//            return;
//        } else if (!StringUtils.checkPassword(pwd)) {
//            showToast("密码不合法");
//            return;
//        }

        //检查权限,请求用户给权限,获得权限

//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
//            //没有被授权
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }else {
//            mLoginPresenter.login(username,pwd);
//        }
        //从本地数据库加载到内存
        EMClient.getInstance().chatManager().loadAllConversations();
        mLoginPresenter.login(username, pwd);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                login();
            }else {
                showToast("您拒绝了我读取内存卡的权限");
            }
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        login();
    }

    @Override
    public void afterLogin(User user, boolean isSuccess, String msg) {
        if (isSuccess){
            //保存user
            saveUser(user);
            //跳转到主界面
            startActivity(MainActivity.class,true);
        }else {
            //弹吐司
            showToast(msg);
        }
    }

    @Override
    public void showProgressDialog(String msg) {
        showDialog(msg,false);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }
}
