package yangchi.cn.myhuanxing.view;

import java.util.List;

/**
 * Created by yangchi on 2018/9/13.
 */
public interface ContactView {

    void showContacts(List<String> contacts);

    void updateContacts(boolean isSuccess);

    void afterContact(boolean isSuccess,String username);
}
