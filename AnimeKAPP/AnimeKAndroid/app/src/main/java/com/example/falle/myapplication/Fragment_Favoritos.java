package com.example.falle.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Fragment_Favoritos extends Fragment {

    private AppBarLayout app;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String name;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_Favoritos() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view= inflater.inflate(R.layout.fragment_fragment__favoritos, container, false);
         View contenedor = (View) container.getParent();
         app= contenedor.findViewById(R.id.appbar);
         tabLayout = new TabLayout(getActivity());
         app.addView(tabLayout);
         name= getArguments().getString("name");




         viewPager= view.findViewById(R.id.view);
         ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
         viewPager.setAdapter(viewPagerAdapter);
         tabLayout.setupWithViewPager(viewPager);



         return  view;

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        app.removeView(tabLayout);
    }
    public class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String[] tabs = {"Anime", "Manga"};

        public Fragment getItem(int position){
            switch (position){
                case 0:
                    Fragment fragment = new Fragment_anime();
                    Bundle b = new Bundle();
                    b.putString("name", name);
                    fragment.setArguments(b);
                    return fragment;

                case 1: return new Fragment_manga();

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
