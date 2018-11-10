package yangchi.cn.myhuanxing.presenter;

import com.hyphenate.chat.EMClient;

import yangchi.cn.myhuanxing.adapter.EMCallBackAdapter;
import yangchi.cn.myhuanxing.utils.ThreadUtil;
import yangchi.cn.myhuanxing.view.PluginFragment;
import yangchi.cn.myhuanxing.view.PluginView;

/**
 * Created by yangchi on 2018/9/13.
 */
public class PluginPresenterImpl implements PluginPresenter{

    private PluginView mPluginView;

    public PluginPresenterImpl(PluginView pluginView) {
        mPluginView=pluginView;
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true,new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                    gotoLogin(true,"");
            }
            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                gotoLogin(false,s);
            }
        });
    }

    private void gotoLogin(final boolean success, final String msg) {
        //主线程
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPluginView.afterLogout(success,msg);
            }
        });

    }
}
