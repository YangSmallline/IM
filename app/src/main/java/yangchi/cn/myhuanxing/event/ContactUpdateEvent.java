package yangchi.cn.myhuanxing.event;

/**
 * Created by yangchi on 2018/9/16.
 */
public class ContactUpdateEvent  {
    public boolean isAdded;
    public String username;

    public ContactUpdateEvent(boolean isAdded, String username) {
        this.isAdded = isAdded;
        this.username = username;
    }

    public ContactUpdateEvent() {
    }


}
