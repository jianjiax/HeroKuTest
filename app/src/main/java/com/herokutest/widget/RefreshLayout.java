package com.herokutest.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.herokutest.R;


public class RefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;
    private View mListViewFooter;
    private int mYDown;
    private int mLastY;
    private boolean isloading = false;
    public boolean isCanLoading = true;//true 可以加载
    private View noDataFooter;
    private TextView tv_noData;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setColorSchemeResources(
                R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

//                if (isPullingUp()) {
//                    //you can add view or hint here when pulling up the ListView
//                }
                mLastY = (int) event.getRawY();
                if (mListView != null && canLoad()) {
                    loadData();
                }
                break;

            case MotionEvent.ACTION_UP:
//                mLastY = (int) event.getRawY();
//                if (mListView != null && canLoad()) {
//                    loadData();
//                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean canLoad() {
        return !isRefreshing() && isBottom() && !isloading && isPullingUp();
    }

    private boolean isBottom() {
        if (mListView.getCount() > 0) {
            if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 &&
                    mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (!isloading) {
            super.setRefreshing(refreshing);
        }
    }

    private boolean isPullingUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    private void loadData() {
        if (mOnLoadListener != null) {
            if (isCanLoading) {
                setLoading(true);
                mOnLoadListener.onLoad();
            }
        } else {
            mListView.removeFooterView(mListViewFooter);
        }
    }

    public void setLoading(boolean loading) {
        isloading = loading;
//        if (isloading) {
//            if (isRefreshing()) setRefreshing(false);
//            if (mListView.getFooterViewsCount() == 0) {
//                mListView.addFooterView(mListViewFooter);
//                mListView.setSelection(mListView.getAdapter().getCount() - 1);
//            } else {
//                mListViewFooter.setVisibility(VISIBLE);
//                //mListView.addFooterView(mListViewFooter);
//            }
//        } else {
//            if (mListView.getAdapter() instanceof HeaderViewListAdapter) {
//                mListView.removeFooterView(mListViewFooter);
//            } else {
//                mListViewFooter.setVisibility(View.GONE);
//            }
//            mYDown = 0;
//            mLastY = 0;
//        }
        if(mListView != null){

            if (isloading) {
                mListView.addFooterView(mListViewFooter);
                mListView.setSelection(mListView.getAdapter().getCount() - 1);
            } else {
                mListView.removeFooterView(noDataFooter);
                mListView.removeFooterView(mListViewFooter);
                mYDown = 0;
                mLastY = 0;
            }
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }


    public interface OnLoadListener {
        void onLoad();
    }

    public boolean isLoading() {
        return isloading;
    }

    public void canLoading(boolean isCanLoading) {
        this.isCanLoading = isCanLoading;
        if (mListView != null && !isCanLoading) {
            mListView.removeFooterView(mListViewFooter);
            mListView.removeFooterView(noDataFooter);
            mListView.addFooterView(noDataFooter);
        } else if (mListView != null) {
            mListView.removeFooterView(mListViewFooter);
            mListView.removeFooterView(noDataFooter);
        }
    }

    public void setFootText(String str) {
        if (tv_noData != null) {
            tv_noData.setText(str);
        }
    }
}
