package com.lisen.android.weixin6_0.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.lisen.android.weixin6_0.R;
import com.lisen.android.weixin6_0.fragment.TabFragment;
import com.lisen.android.weixin6_0.view.MyTabView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private String[] mTitles;
    private List<TabFragment> mTabFragments = new ArrayList<>();

    public static final int TAB_START = 0;
    public static final int TAB_FRIEND_LIST = 1;
    public static final int TAB_EMOTICON = 2;
    public static final int TAB_ALL_FRIENDS = 3;

    private List<MyTabView> mMyTabViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将默认的三个点显示出来
        setOverflowButtonAlways();
        //将actionbar上的图标去掉
        if (getActionBar() != null) {
            getActionBar().setDisplayShowHomeEnabled(false);
        }
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {
        for (MyTabView myTabView : mMyTabViews) {
            myTabView.setOnClickListener(this);
        }
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        mTitles = new String[]{
                "微信", "通讯录", "发现", "我"
        };


    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main_activity);

        //将fragment添加到集合
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mTabFragments.add(tabFragment);
        }

        //将view添加到集合
        MyTabView tabStart = (MyTabView) findViewById(R.id.tab_start_main_activity);
        mMyTabViews.add(tabStart);
        MyTabView tabFriendList = (MyTabView) findViewById(R.id.tab_friend_list_main_activity);
        mMyTabViews.add(tabFriendList);
        MyTabView tabEmoticons = (MyTabView) findViewById(R.id.tab_emoticons_main_activity);
        mMyTabViews.add(tabEmoticons);
        MyTabView tabAllFriends = (MyTabView) findViewById(R.id.tab_all_friends_main_activity);
        mMyTabViews.add(tabAllFriends);
        //默认第一个为有颜色的
        tabStart.setIconAlpha(1.0f);
        tabFriendList.setIconAlpha(0.0f);
        tabEmoticons.setIconAlpha(0.0f);
        tabAllFriends.setIconAlpha(0.0f);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabFragments.get(position);
            }

            @Override
            public int getCount() {
                return mTabFragments.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_interface, menu);
        return true;
    }

    /**
     * 将actionbar上默认的三个点显示出来
     */
    private void setOverflowButtonAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置menu显示图标
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) // 找到实现类
            {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        clearAlpha();
        switch (v.getId()) {
            case R.id.tab_start_main_activity:
                mMyTabViews.get(TAB_START).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(TAB_START, false);
                break;
            case R.id.tab_friend_list_main_activity:
                mMyTabViews.get(TAB_FRIEND_LIST).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(TAB_FRIEND_LIST, false);
                break;
            case R.id.tab_emoticons_main_activity:
                mMyTabViews.get(TAB_EMOTICON).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(TAB_EMOTICON, false);
                break;
            case R.id.tab_all_friends_main_activity:
                mMyTabViews.get(TAB_ALL_FRIENDS).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(TAB_ALL_FRIENDS, false);
                break;
            default:
                break;
        }
    }

    /**
     * 恢复原色
     */
    private void clearAlpha() {
        for (MyTabView myTabView : mMyTabViews) {
            myTabView.setIconAlpha(0.0f);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset > 0) {
            MyTabView left = mMyTabViews.get(position);
            MyTabView right = mMyTabViews.get(position + 1);
            left.setIconAlpha(1.0f - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
