package com.example.grocerystoreoffers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;



import android.content.SharedPreferences;
import android.content.res.Configuration;


import android.content.res.Resources;


import android.os.Build;


import android.os.Bundle;


import android.util.DisplayMetrics;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;


import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;


import android.view.View;


import android.view.ViewGroup;


import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    ArrayList<Product> arrayList;
    ListView lv;
    HomeCustomListAdapter homeCustomListAdapter;

    private Button sweBtn, engBtn;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;
    FirebaseFirestore fStore;
    FirebaseUser user;

    private String mParam1;
    private String mParam2;
    Boolean ica = false, coop = false,lidl = false,willys = false;
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

        arrayList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lv = view.findViewById(R.id.list);



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


        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if(document.getBoolean("ICA")){

                            ica=true;
                            Log.d("STOREBOOL ICA ",String.valueOf(ica));
                        }
                        if(document.getBoolean("COOP")){
                            Log.d("STOREBOOL","COOP TRUE");
                            coop=true;
                        }
                        if(document.getBoolean("LIDL")){

                            lidl=true;
                            Log.d("STOREBOOL LIDL ",String.valueOf(lidl));
                        }
                        if(document.getBoolean("Willys")){
                            Log.d("STOREBOOL","WILLYS TRUE");
                            willys=true;
                        }
                        filterStore();
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://130.243.20.190:5000/app/products/");
            }
        });

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

    public void filterStore() {

        if (!coop && !ica && !lidl && !willys) {

            HomeCustomListAdapter adapter = new HomeCustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.home_custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);

        } else {
            ArrayList<Product> filteredList = new ArrayList<>();
            homeCustomListAdapter = new HomeCustomListAdapter(getActivity().getApplicationContext(), R.layout.home_custom_list_layout, filteredList);
            for (Product product : arrayList) {
                if (ica) {
                    Log.d("FILTERSTORE", "ICA FOUND");
                    if (product.getStore().equals("3")) {
                        filteredList.add(product);
                    }
                }
                if (coop) {
                    if (product.getStore().equals("2")) {
                        filteredList.add(product);
                    }
                }
                if (lidl) {
                    Log.d("FILTERSTORE", "LIDL FOUND");
                    if (product.getStore().equals("1")) {
                        filteredList.add(product);
                    }
                }
                if (willys) {
                    if (product.getStore().equals("4")) {
                        filteredList.add(product);
                    }
                }
            }

            homeCustomListAdapter.setFilteredList(filteredList);
            HomeCustomListAdapter adapter = new HomeCustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.home_custom_list_layout, filteredList
            );
            lv.setAdapter(adapter);
        }
    }

    private void filterList(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();
        homeCustomListAdapter = new HomeCustomListAdapter(getActivity().getApplicationContext(), R.layout.home_custom_list_layout, filteredList);
        for (Product product : arrayList)  {
            if (product.getName().toLowerCase().contains(text.toLowerCase()))   {
                filteredList.add(product);
            }
            else if(product.getId().equals(text)){
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty())   {
            Toast toast = Toast.makeText(getActivity(), "No product was found",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }   else    {
            homeCustomListAdapter.setFilteredList(filteredList);
            HomeCustomListAdapter adapter = new HomeCustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.home_custom_list_layout, filteredList
            );
            lv.setAdapter(adapter);
        }
    }


    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {

            try {
                content = content.replace("\\\"", "\"");
                Log.d("content",content);
                content = content.substring(1);


                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Product(
                            productObject.getString("id"),
                            productObject.getString("image"),
                            productObject.getString("name"),
                            productObject.getString("price"),
                            productObject.getString("store_id")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HomeCustomListAdapter adapter = new HomeCustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.home_custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);
        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }



}