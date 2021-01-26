package com.fei.parallaxviewpager.parallax;

/**
 * @ClassName: ParallaxTag
 * @Description: 描述
 * @Author: Fei
 * @CreateDate: 2021/1/26 11:13
 * @UpdateUser: Fei
 * @UpdateDate: 2021/1/26 11:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ParallaxTag {

    public float translationXIn, translationXOut, translationYIn, translationYOut;

    @Override
    public String toString() {
        return "ParallaxTag{" +
                "translationXIn=" + translationXIn +
                ", translationXOut=" + translationXOut +
                ", translationYIn=" + translationYIn +
                ", translationYOut=" + translationYOut +
                '}';
    }
}
