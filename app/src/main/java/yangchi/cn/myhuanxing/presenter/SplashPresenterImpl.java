package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;

import yangchi.cn.myhuanxing.view.SplashView;

/**
 * Created by yangchi on 2018/9/11.
 */
public class SplashPresenterImpl implements  SplashPresenter{

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void isLogined() {
        if (EMClient.getInstance().isLoggedInBefore()&&EMClient.getInstance().isConnected()){
            //已经登录过了
            mSplashView.checkLogined(true);
        }else {
            //没有登录
            mSplashView.checkLogined(false);
        }
    }
}
