package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;

import yangchi.cn.myhuanxing.adapter.EMCallBackAdapter;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.view.LoginView;

/**
 * Created by yangchi on 2018/9/11.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView){
        mLoginView=loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        //显示对话框
        mLoginView.showProgressDialog("正在登录");
        EMClient.getInstance().login(username, pwd, new EMCallBackAdapter() {
            @Override
            public void onSuccess() {
                hideDialog(new User(username,pwd),true,"");
            }

            @Override
            public void onError(int i, String s) {
                hideDialog(new User(username,pwd),false,s);
            }
        });
    }
    private void hideDialog(final User user, final boolean isSuccess, final String error) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoginView.afterLogin(user, isSuccess, error);
                mLoginView.hideProgressDialog();
            }
        });
    }
}
