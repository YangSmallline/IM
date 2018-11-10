package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import yangchi.cn.myhuanxing.model.User;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.utils.ToastUtil;
import yangchi.cn.myhuanxing.view.RegistVIew;

/**
 * Created by yangchi on 2018/9/12.
 */
public class RegisterPresenterImpl implements RegistPresenter {
    RegistVIew mRegistVIew;

    public RegisterPresenterImpl(RegistVIew registVIew) {
        mRegistVIew = registVIew;
    }
    @Override
    public void regist(final String username, final String pwd) {
        //显示进度条对话框
        //放在主线程
        mRegistVIew.showProgressDialog("正在注册");
        //隐藏精度条对话框
        User user = new User();
        user.setPassword(pwd);
        user.setUsername(username);

        user.signUp(new SaveListener<User>() {
            @Override//在主线程回调
            public void done(final User user, BmobException e) {
                if (e == null) {//在bomb存储成功
                    //到子线程上运行
                    ThreadUtil.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //注册成功
                                EMClient.getInstance().createAccount(username, pwd);
                                //调用主线程开启对话框并且调用注册后的函数
                                mRegistVIew.hideProgressDialog();
                                mRegistVIew.afterRegist(user, true,"");

                            } catch (HyphenateException e1) {
                                e1.printStackTrace();
                                //注册失败
                                user.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            mRegistVIew.hideProgressDialog();
                                            mRegistVIew.afterRegist(user, false,e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {//在bomb存储失败
                    mRegistVIew.hideProgressDialog();
                    mRegistVIew.afterRegist(user, false,e.getMessage());
                }
            }
        });
    }
}
