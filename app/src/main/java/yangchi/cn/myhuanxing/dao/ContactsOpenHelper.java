package yangchi.cn.myhuanxing.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangchi on 2018/9/13.
 */
public class ContactsOpenHelper extends SQLiteOpenHelper {

    private ContactsOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ContactsOpenHelper(Context context){
        this(context,"contacts",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_contact(_id integer primary key,username varchar(20),contact varcahr(20) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
