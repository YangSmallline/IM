package yangchi.cn.myhuanxing.view;

import yangchi.cn.myhuanxing.model.User;

/**
 * Created by yangchi on 2018/9/11.
 */
public interface LoginView extends BaseView{
    void afterLogin(User user,boolean isSuccess,String msg);
}
