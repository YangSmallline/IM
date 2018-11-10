package yangchi.cn.myhuanxing.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yangchi.cn.myhuanxing.R;

/**
 * Created by yangchi on 2018/9/13.
 */
public abstract  class BaseFragment extends Fragment {

    public TextView mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTitle = getView().findViewById(R.id.tv_title);
        initTitle(mTitle);
    }

    protected abstract void initTitle(TextView mTitle);

}
