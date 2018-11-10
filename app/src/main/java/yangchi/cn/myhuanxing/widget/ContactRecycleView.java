package yangchi.cn.myhuanxing.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import yangchi.cn.myhuanxing.R;

/**
 * Created by yangchi on 2018/9/13.
 */
public class ContactRecycleView extends RelativeLayout {


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSrl_contact;

    //new
    public ContactRecycleView(Context context) {
        this(context,null);
    }

    //xml
    public ContactRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

        //TODO
        LayoutInflater.from(context).inflate(R.layout.contact_list_layout,this,true);
        mRecyclerView = findViewById(R.id.recyclerview);
        mSrl_contact = findViewById(R.id.srl_contact);
        mSrl_contact.setColorSchemeResources(R.color.mainColor);
    }

    public ContactRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    public void setAdapter(RecyclerView.Adapter adapter){
        //
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    //封装监听器
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){

        mSrl_contact.setOnRefreshListener(listener);
    }

    public void setRefreshing(boolean b) {
        if (mSrl_contact!=null){
            mSrl_contact.setRefreshing(b);
        }
    }
}
