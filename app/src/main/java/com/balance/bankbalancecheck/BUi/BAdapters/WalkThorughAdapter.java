package com.balance.bankbalancecheck.BUi.BAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.balance.bankbalancecheck.BUi.BActivities.WalkThroughActivity;
import com.balance.bankbalancecheck.R;

public class WalkThorughAdapter extends PagerAdapter {
    private Activity activity;
    private Integer[] imagesArray;
    private String[] text, subText;

    public WalkThorughAdapter(Activity activity, Integer[] images, String[] text, String[] subText) {
        this.activity = activity;
        this.imagesArray = images;
        this.text = text;
        this.subText = subText;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.item_pager_list, container, false);
        ImageView IvSlider = viewItem.findViewById(R.id.IvSlider);
        TextView TvWalkTitle = (TextView) viewItem.findViewById(R.id.TvWalkTitle);
        TextView TvWalkSubTitle = (TextView) viewItem.findViewById(R.id.TvWalkSubTitle);
        TvWalkTitle.setText(text[position]);
        TvWalkSubTitle.setText(subText[position]);

        IvSlider.setImageResource(imagesArray[position]);
        container.addView(viewItem);


        return viewItem;
    }

    @Override
    public int getCount() {
        return imagesArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
