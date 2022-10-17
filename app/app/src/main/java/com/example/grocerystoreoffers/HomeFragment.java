package com.example.grocerystoreoffers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Bundle;



import android.content.SharedPreferences;
import android.content.res.Configuration;


import android.content.res.Resources;


import android.os.Build;


import android.os.Bundle;


import android.util.DisplayMetrics;




import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;


import androidx.fragment.app.FragmentTransaction;







import android.view.LayoutInflater;


import android.view.View;


import android.view.ViewGroup;


import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.AuthResult;

import java.util.Locale;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private Button sweBtn, engBtn;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;

    private String mParam1;
    private String mParam2;
    private Button button;

    public HomeFragment() {
        // Required empty public constructor
    }


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
        loadData();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        engBtn = (Button) view.findViewById(R.id.engBtn);
        engBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveData("en");
            }
        });

        sweBtn = (Button) view.findViewById(R.id.sweBtn);
        sweBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveData("sv");
            }
        });
        engBtn.setText(R.string.eng_Btn);
        sweBtn.setText(R.string.swe_Btn);
        return view;
    }

    private void setAppLocale(String localeCode){

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }   else   {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    public void saveData(String lang)   {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, lang);
        editor.commit();
        Toast.makeText(getActivity(), "Data saved", Toast.LENGTH_SHORT).show();
        getActivity().recreate();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    public static View replaceView(LayoutInflater inflater, View currentView, int newViewId) {

        View newView = inflater.inflate(newViewId, null);
        return replaceView(inflater, currentView, newView);
    }

    public static View replaceView(LayoutInflater inflater, View currentView, View newView) {

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
}