package yangchi.cn.myhuanxing.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.model.User;

/**
 * Created by yangchi on 2018/9/14.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<User> mUserList;

    private List<String> contactList;

    public SearchAdapter(List<User> userList, List<String> contactList) {
        mUserList = userList;
        this.contactList = contactList;
    }

    private OnAddFriendCLickListener mOnAddFriendCLickListener;

    public void setOnAddFriendClickListener(OnAddFriendCLickListener onAddFriendClickListener) {
        mOnAddFriendCLickListener = onAddFriendClickListener;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search, viewGroup, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        final User user = mUserList.get(i);

        searchViewHolder.tvUsername.setText(user.getUsername());
        searchViewHolder.tvTime.setText(user.getCreatedAt());

        if (contactList.contains(user.getUsername())) {
            searchViewHolder.btnAdd.setText("已是好友");
            searchViewHolder.btnAdd.setEnabled(false);
            searchViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddFriendCLickListener.onClick("已是好友");
                    Log.i("tag", "onClick: 已是好友被点击");
                }
            });
        } else {
            searchViewHolder.btnAdd.setText("添加");
            searchViewHolder.btnAdd.setEnabled(true);
            searchViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tag", "onClick: 添加好友被点击");
                    mOnAddFriendCLickListener.onClick(user.getUsername());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }


    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvTime;
        Button btnAdd;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnAdd = itemView.findViewById(R.id.btn_add_one);
        }
    }

    public interface OnAddFriendCLickListener {
        void onClick(String username);
    }

}

