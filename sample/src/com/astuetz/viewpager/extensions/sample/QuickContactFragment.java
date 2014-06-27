package com.astuetz.viewpager.extensions.sample;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Random;

public class QuickContactFragment extends DialogFragment {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private ContactPagerAdapter adapter;

    public static QuickContactFragment newInstance() {
        QuickContactFragment f = new QuickContactFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }

        View root = inflater.inflate(R.layout.fragment_quick_contact, container, false);

        tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
        pager = (ViewPager) root.findViewById(R.id.pager);
        adapter = new ContactPagerAdapter();

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

        return root;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();

        // change dialog width
        if (getDialog() != null) {

            int fullWidth = getDialog().getWindow().getAttributes().width;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                fullWidth = size.x;
            } else {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                fullWidth = display.getWidth();
            }

            final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                    .getDisplayMetrics());

            int w = fullWidth - padding;
            int h = getDialog().getWindow().getAttributes().height;

            getDialog().getWindow().setLayout(w, h);
        }
    }

    public class ContactPagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.TabBackgroundProvider, PagerSlidingTabStrip.TabCustomViewProvider {

        private final int[] ICONS = { R.drawable.ic_launcher_gplus, R.drawable.ic_launcher_gmail,
                R.drawable.ic_launcher_gmaps, R.drawable.ic_launcher_chrome };

        private final View[] customViews = new View[ICONS.length];
        private final View[] backgroundViews = new View[ICONS.length];
        private final Random random = new Random();

        private ImageView createImageView(final int position) {

            final ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(ICONS[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            return imageView;
        }

        @SuppressWarnings("deprecation")
        @SuppressLint("NewApi")
        private View createBackground(final int position) {

            final Resources resources = getActivity().getResources();
            final int selectedColor = Color.HSVToColor(new float[] { 360.0f * random.nextFloat(), 1.0f, 1.0f });
            final int highlightColor = Color.HSVToColor(new float[] { 360.0f * random.nextFloat(), 1.0f, 1.0f });

            final StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(highlightColor));
            stateListDrawable.addState(new int[] { android.R.attr.state_focused }, new ColorDrawable(highlightColor));
            stateListDrawable.addState(new int[] { android.R.attr.state_selected }, new ColorDrawable(selectedColor));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(Color.TRANSPARENT));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                stateListDrawable.setExitFadeDuration(resources.getInteger(android.R.integer.config_shortAnimTime));
            }

            final View view = new View(getActivity());
            view.setDuplicateParentStateEnabled(true);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(stateListDrawable);
            } else {
                view.setBackground(stateListDrawable);
            }

            return view;
        }

        public ContactPagerAdapter() {
            super();

            for (int i = 0; i < ICONS.length; i++) {
                customViews[i] = createImageView(i);
                backgroundViews[i] = createBackground(i);
            }
        }

        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public View getPageTabBackground(final int position) {
            return backgroundViews[position];
        }

        @Override
        public View getPageTabCustomView(final int position) {
            return customViews[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // looks a little bit messy here
            TextView v = new TextView(getActivity());
            v.setBackground(new ColorDrawable(getResources().getColor(R.color.background_window)));
            v.setText("PAGE " + (position + 1));
            final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
                    .getDisplayMetrics());
            v.setPadding(padding, padding, padding, padding);
            v.setGravity(Gravity.CENTER);
            container.addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View v, Object o) {
            return v == ((View) o);
        }

    }

}