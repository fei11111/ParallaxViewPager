package com.fei.parallaxviewpager.parallax;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ParallaxViewPager
 * @Description: 描述
 * @Author: Fei
 * @CreateDate: 2021/1/25 17:17
 * @UpdateUser: Fei
 * @UpdateDate: 2021/1/25 17:17
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ParallaxViewPager extends ViewPager {

    private List<ParallaxFragment> parallaxFragmentList;

    public ParallaxViewPager(@NonNull Context context) {
        this(context, null);
    }

    public ParallaxViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        parallaxFragmentList = new ArrayList<>();
    }

    /**
     * 直接传入布局，后面效果交由ParallaxFragment和ParallaxViewPager完成
     *
     * @param layouts 布局id
     */
    public void setLayouts(FragmentManager fm,@LayoutRes int... layouts) {
        //创建fragment
        for (int layout : layouts) {
            ParallaxFragment parallaxFragment = ParallaxFragment.newInstance(layout);
            parallaxFragmentList.add(parallaxFragment);
        }
        ParallaxFragmentAdapter adapter = new ParallaxFragmentAdapter(fm,parallaxFragmentList);
        setAdapter(adapter);
    }
}
