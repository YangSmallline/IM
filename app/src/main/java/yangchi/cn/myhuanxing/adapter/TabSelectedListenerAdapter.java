package yangchi.cn.myhuanxing.adapter;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

/**
 * Created by yangchi on 2018/9/13.
 */
public abstract class TabSelectedListenerAdapter implements BottomNavigationBar.OnTabSelectedListener {


    @Override
    public abstract void onTabSelected(int position);

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

}
