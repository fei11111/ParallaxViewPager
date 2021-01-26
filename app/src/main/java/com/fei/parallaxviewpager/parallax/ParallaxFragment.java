package com.fei.parallaxviewpager.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.Fragment;

import com.fei.parallaxviewpager.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import static androidx.appcompat.widget.VectorEnabledTintResources.MAX_SDK_WHERE_REQUIRED;

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
public class ParallaxFragment extends Fragment implements LayoutInflater.Factory2 {

    //布局id
    private static final String EXTRA_LAYOUT_ID = "extra_layout_id";
    //源码的createView方法为final无法调用，直接将源码拷过来，并修改访问修饰符
    private ParallaxCompatViewInflater mAppCompatViewInflater;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    //自定义属性
    private int[] mAttrs = new int[]{
            R.attr.translationXIn, R.attr.translationXOut,
            R.attr.translationYIn, R.attr.translationYOut
    };
    //保存存在自定义属性的View
    public List<View> views = new ArrayList<>();

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
            //新复制一个，怕影响原来源码逻辑
            LayoutInflater layoutInflater = inflater.cloneInContext(getActivity());
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
            return layoutInflater.inflate(layoutId, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createView(parent, name, context, attrs);
        if (view != null) {
            //获取attrs自定义属性
            ParallaxTag parallaxTag = new ParallaxTag();
            //类似R.styleable也是数组
            TypedArray typedArray = context.obtainStyledAttributes(attrs, mAttrs);
            if (typedArray != null && typedArray.getIndexCount() > 0) {
                //这里需要注意是拿下标的值，类似R.styleable.xxx_xxx
                parallaxTag.translationXIn = typedArray.getFloat(0, 0);
                parallaxTag.translationXOut = typedArray.getFloat(1, 0);
                parallaxTag.translationYIn = typedArray.getFloat(2, 0);
                parallaxTag.translationYOut = typedArray.getFloat(3, 0);
                view.setTag(R.id.parallax_tag, parallaxTag);
                views.add(view);
                typedArray.recycle();
            }
        }
        return view;
    }

    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return createView(null, name, context, attrs);
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.AppCompatTheme);
            String viewInflaterClassName =
                    a.getString(R.styleable.AppCompatTheme_viewInflaterClass);
            if (viewInflaterClassName == null) {
                // Set to null (the default in all AppCompat themes). Create the base inflater
                // (no reflection)
                mAppCompatViewInflater = new ParallaxCompatViewInflater();
            } else {
                try {
                    Class<?> viewInflaterClass = Class.forName(viewInflaterClassName);
                    mAppCompatViewInflater =
                            (ParallaxCompatViewInflater) viewInflaterClass.getDeclaredConstructor()
                                    .newInstance();
                } catch (Throwable t) {
//                    Log.i(TAG, "Failed to instantiate custom view inflater "
//                            + viewInflaterClassName + ". Falling back to default.", t);
                    mAppCompatViewInflater = new ParallaxCompatViewInflater();
                }
            }
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : true;
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                false && Build.VERSION.SDK_INT <= MAX_SDK_WHERE_REQUIRED /* Only tint wrap the context if enabled */
        );
    }
}
