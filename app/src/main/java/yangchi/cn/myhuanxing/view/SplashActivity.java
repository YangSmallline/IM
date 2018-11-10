package yangchi.cn.myhuanxing.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import yangchi.cn.myhuanxing.MainActivity;
import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.common.BaseActivity;
import yangchi.cn.myhuanxing.presenter.SplashPresenter;
import yangchi.cn.myhuanxing.presenter.SplashPresenterImpl;

public class SplashActivity extends BaseActivity implements SplashView {


    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //判断是否登录过
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.isLogined();//调用checkLogined

    }


    @Override
    public void checkLogined(boolean isLogined) {
        if (isLogined) {
            //跳转到主界面
            startActivity(MainActivity.class, true);
        } else {
            //跳转到登录界面
            ObjectAnimator.ofFloat(ivSplash, "alpha", 0f, 1).setDuration(2000).start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showToast("跳转到登录界面");
                    startActivity(LoginActivity.class,true);
                }
            }, 2000);
        }
    }
}
