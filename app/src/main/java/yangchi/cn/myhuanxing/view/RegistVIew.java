package yangchi.cn.myhuanxing.view;

import yangchi.cn.myhuanxing.model.User;

/**
 * Created by yangchi on 2018/9/12.
 */
public interface RegistVIew extends BaseView{

    void afterRegist(User user,boolean b,String msg);
}
