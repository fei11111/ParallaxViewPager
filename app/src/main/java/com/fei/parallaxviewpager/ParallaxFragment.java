package com.fei.parallaxviewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @ClassName: ParallaxFragment
 * @Description: 描述
 * @Author: Fei
 * @CreateDate: 2021/1/25 16:22
 * @UpdateUser: Fei
 * @UpdateDate: 2021/1/25 16:22
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ParallaxFragment extends Fragment {

    private static final String EXTRA_LAYOUT_ID = "extra_layout_id";

    public static ParallaxFragment newInstance(@LayoutRes int layoutId) {
        ParallaxFragment fragment = new ParallaxFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LAYOUT_ID, layoutId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            int layoutId = getArguments().getInt(EXTRA_LAYOUT_ID);
            return inflater.inflate(layoutId, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
