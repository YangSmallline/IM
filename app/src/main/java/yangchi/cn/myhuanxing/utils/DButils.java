package yangchi.cn.myhuanxing.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import yangchi.cn.myhuanxing.dao.ContactsOpenHelper;

/**
 * Created by yangchi on 2018/9/13.
 */
public class DButils {

    private static ContactsOpenHelper contactsOpenHelper;

    private static boolean inited=false;

    public static void init(Context context){
        contactsOpenHelper = new ContactsOpenHelper(context);
        inited=true;
    }

    public static void saveContacts(String username,List<String> contacts){

        if (!inited){
            throw new RuntimeException("没有初始化DBUtils,请先初始化");
        }
        //开启事务提高效率(因为它自走在内存里面的),走一次io,
        //开启一个事务,要么全成功,要么全失败,完不不了就回退,回滚
        SQLiteDatabase database=contactsOpenHelper.getReadableDatabase();
        database.beginTransaction();
//        database.execSQL("delete from t_contact where username='"+username+"'");
        //在存入数据之前，先清空数据
        database.delete("t_contact","username=?",new String[]{username});

        ContentValues values=new ContentValues();
        for (int i=0;i<contacts.size();i++){
            values.put("username",username);//每次插入一行
            values.put("contact",contacts.get(i));//每次插入一行
            database.insert("t_contact",null,values);//如果不开启事务,每次insert都会打开一次磁盘
        }
        //提交事务
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public static List<String> getContacts(String username){
        List<String> contactList=new ArrayList<>();
        SQLiteDatabase database=contactsOpenHelper.getReadableDatabase();
        Cursor cursor = database.query("t_contact", new String[]{"contact"}, "username=?", new String[]{username}, null, null, "contact");//desc asc
        while (cursor.moveToNext()){
            String contact=cursor.getString(0);
            contactList.add(contact);
        }
        cursor.close();
        return contactList;
    }
}
