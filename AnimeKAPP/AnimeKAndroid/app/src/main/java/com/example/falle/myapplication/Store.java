package com.example.falle.myapplication;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Store extends Fragment {
    private AppBarLayout app;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_store, container, false);
        View contenedor = (View) container.getParent();
        app= contenedor.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        app.addView(tabLayout);





        viewPager= view.findViewById(R.id.view);
        Store.ViewPagerAdapter viewPagerAdapter = new Store.ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        return  view;

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        app.removeView(tabLayout);
    }
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String[] tabs = {"T-Shirt", "Figures"};

        public Fragment getItem(int position){
            switch (position){
                case 0:
                    Fragment fragment = new Fragment_camiseta();
                    return fragment;

                case 1: return new Fragment_figuras();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
        public CharSequence getPageTitle(int position){
            return tabs[position];


        }

    }
}