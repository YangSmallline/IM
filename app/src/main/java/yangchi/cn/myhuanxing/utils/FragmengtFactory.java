package yangchi.cn.myhuanxing.utils;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import yangchi.cn.myhuanxing.view.BaseFragment;
import yangchi.cn.myhuanxing.view.ContactsFragment;
import yangchi.cn.myhuanxing.view.ConversationFragment;
import yangchi.cn.myhuanxing.view.PluginFragment;

/**
 * Created by yangchi on 2018/9/13.
 */
public class FragmengtFactory {

    private static ConversationFragment conversationFragment;
    private static ContactsFragment contactsContract;
    private static PluginFragment pluginFragment;

    public static ConversationFragment getConversationFragment(){
        if (conversationFragment==null){
            conversationFragment=new ConversationFragment();
        }
        return conversationFragment;
    }
    public static ContactsFragment getContactsContract(){
        if (contactsContract==null){
            contactsContract=new ContactsFragment();
        }
        return contactsContract;
    }
    public static PluginFragment getPluginFragment(){
        if (pluginFragment==null){
            pluginFragment=new PluginFragment();
        }
        return pluginFragment;
    }

    public static BaseFragment getFragment(int index){
        switch (index){
            case 0:
                return getConversationFragment();
            case 1:
                return getContactsContract();
            case 2:
                return getPluginFragment();
        }
        return null;
    }
}
