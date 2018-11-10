package yangchi.cn.myhuanxing.event;

import com.hyphenate.chat.EMMessage;

/**
 * Created by yangchi on 2018/9/17.
 */
public class OnNewMsgEvent {

    public EMMessage mMessage;

    public OnNewMsgEvent(EMMessage message) {
        mMessage = message;
    }

}
