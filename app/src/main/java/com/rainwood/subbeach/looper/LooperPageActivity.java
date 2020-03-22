package com.rainwood.subbeach.looper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rainwood.subbeach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/3/22 13:41
 * @Desc: java类作用描述
 */
public final class LooperPageActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<Integer> mData = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_page);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = this.findViewById(R.id.vp_pager);
        // abstract pagerAdapter
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initData() {
        mData.add(R.mipmap.img0);
        mData.add(R.mipmap.img1);
        mData.add(R.mipmap.img2);
        mData.add(R.mipmap.img3);
        mPagerAdapter.notifyDataSetChanged();
        // 设置到中间位置
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 初始化方法
         * @param container
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager, container, false);
            ImageView ivCover = view.findViewById(R.id.iv_cover);
            // 设置数据
            int realPosition = position % mData.size();
            ivCover.setImageResource(mData.get(realPosition));
            if (ivCover.getParent() instanceof ViewGroup) {
                ((ViewGroup) ivCover.getParent()).removeView(ivCover);
            }
            container.addView(ivCover);
            return ivCover;
        }

        /**
         * 销毁方法
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
           // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    };

}
