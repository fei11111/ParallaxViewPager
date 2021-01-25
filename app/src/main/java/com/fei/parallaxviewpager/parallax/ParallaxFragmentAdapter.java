package com.fei.parallaxviewpager.parallax;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @ClassName: ParallaxFragmentAdapter
 * @Description: 描述
 * @Author: Fei
 * @CreateDate: 2021/1/25 17:23
 * @UpdateUser: Fei
 * @UpdateDate: 2021/1/25 17:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ParallaxFragmentAdapter extends FragmentPagerAdapter {

    private final List<ParallaxFragment> fragments;

    public ParallaxFragmentAdapter(@NonNull FragmentManager fm, List<ParallaxFragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
