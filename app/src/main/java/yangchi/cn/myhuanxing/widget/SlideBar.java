package yangchi.cn.myhuanxing.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;

import java.util.Arrays;
import java.util.List;

import yangchi.cn.myhuanxing.R;
import yangchi.cn.myhuanxing.adapter.ContactAdapter;
import yangchi.cn.myhuanxing.utils.StringUtils;

/**
 * Created by yangchi on 2018/9/13.
 */
public class SlideBar extends View {

    private static final String[] sections = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    private Paint mPaint;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private float mAvgHeight;
    private TextView mToastView;
    private RecyclerView mRecyclerView;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                showToastAndScroll(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //隐藏toast,设置背景为透明
                mToastView.setVisibility(INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return true;
    }

    private void showToastAndScroll(float y) {
        if (mToastView == null) {
            ViewGroup parent = (ViewGroup) getParent();//得到它的父控件
            mToastView = parent.findViewById(R.id.tv_floatview);
            mRecyclerView = parent.findViewById(R.id.recyclerview);
        }
        mToastView.setVisibility(VISIBLE);
//        setBackgroundResource(R.drawable.slide_bg);
        int index = (int) (y / mAvgHeight);
        if (index < 0) {
            index = 0;
        } else if (index > sections.length - 1) {
            index = sections.length;
        }
        String slideBarSection = sections[index];
        mToastView.setText(slideBarSection);
        //定位co

//        ContactAdapter adapter = (ContactAdapter) mRecyclerView.getAdapter();
//        List<String> data = adapter.getData();
//        for (int i=0;i<data.size();i++){
//            if (StringUtils.getInital(data.get(i)).equals(slideBarSection)){
//                mRecyclerView.smoothScrollToPosition(i);
//                return;
//            }
//        }

        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (!(adapter instanceof SectionIndexer)) {
            return;
        }

        SectionIndexer sectionIndexer = (SectionIndexer) adapter;

        //获取真实中所有的分区
        String[] sections = (String[]) sectionIndexer.getSections();

        //当前用于点击的是J，然后需要找到J在真实分区中占的脚标
        int sectionIndex = Arrays.binarySearch(sections, slideBarSection);
        if (sectionIndex < 0) {
            return;
        }
        //根据section的脚标找到条目的脚标
        int positionForSection = sectionIndexer.getPositionForSection(sectionIndex);
        //根据条目的脚标让RecyclerView定位到该位置
            mRecyclerView.scrollToPosition(positionForSection);
    }

    //在布局填充器里面弄不同的上下文,效果是不一样的,activity会给一个默认主题,如果不是,的话丢失默认主题,很难看可能就变成黑色的
    //https://possiblemobile.com/2013/06/context/
    //layout inflation只能用在acitivity
    private void initView(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#8c8c8c"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(context, 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //系统自动转换单位为px
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight() - getPaddingBottom();
        mAvgHeight = (mMeasuredHeight + 0.0f) / sections.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = mMeasuredWidth / 2;
        for (int i = 0; i < sections.length; i++) {
            float y = mAvgHeight * (i + 1);
            canvas.drawText(sections[i], x, y, mPaint);
        }
    }
}
