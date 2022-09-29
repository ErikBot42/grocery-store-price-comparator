package com.example.grocerystoreoffers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button button;
    private FirebaseAuth mAuth;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

    }


    /*
    public void openMapsActivityRaw() {
        Intent intent = new Intent(this, MapsActivityRaw.class);
        startActivity(intent);
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        button = view.findViewById(R.id.logOutButton);


        button.setOnClickListener(view1 -> {
            logOut();
        });
        return view;
    }

    public static View replaceView(LayoutInflater inflater,
                                   View currentView, int newViewId) {
        View newView = inflater.inflate(newViewId, null);
        return replaceView(inflater, currentView, newView);
    }//  w  ww  .jav  a  2s. c om

    public static View replaceView(LayoutInflater inflater,
                                   View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return null;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        parent.addView(newView, index);
        return newView;
    }

    public static ViewGroup getParent(View view) {
        return (ViewGroup) view.getParent();
    }

    public static void removeView(View view) {
        if (view != null) {
            ViewGroup parent = getParent(view);
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void logOut(){
        mAuth.signOut();
    }


}