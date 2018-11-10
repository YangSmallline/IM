package yangchi.cn.myhuanxing.view;

import com.hyphenate.chat.EMMessage;

import java.util.List; /**
 * Created by yangchi on 2018/9/16.
 */
public interface ChatView {


    void afterInitData(List<EMMessage> emMessages,boolean isSmooth);


    void notifyData(boolean success, String msg, EMMessage message);
}
