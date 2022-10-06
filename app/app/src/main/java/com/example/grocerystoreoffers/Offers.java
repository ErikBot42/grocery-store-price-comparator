package com.example.grocerystoreoffers;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offers extends Fragment {

    ArrayList<Product> arrayList;
    ListView lv;

    private SearchView searchView;
    private Toolbar toolbar;
    CustomListAdapter customListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offers.
     */
    // TODO: Rename and change types and number of parameters
    public static Offers newInstance(String param1, String param2) {
        Offers fragment = new Offers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_offers);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrayList = new ArrayList<>();
        View contentView = inflater.inflate(R.layout.fragment_offers, container, false);
        lv = contentView.findViewById(R.id.listView);

        CheckBox favBox = contentView.findViewById(R.id.favouriteStore);
        CheckBox icaBox = contentView.findViewById(R.id.icaStore);
        CheckBox coopBox = contentView.findViewById(R.id.coopStore);
        CheckBox lidlBox = contentView.findViewById(R.id.lidlStore);
        CheckBox willysBox = contentView.findViewById(R.id.willysStore);

        icaBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("3");
            }
        });

        coopBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("2");
            }
        });

        lidlBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("1");
            }
        });

        willysBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("4");
            }
        });


        searchView = contentView.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("https://raw.githubusercontent.com/ErikBot42/grocery-store-price-comparator/main/products.json");
            }
        });
        return contentView;
    }

    private void filterStore(String text)  {

        ArrayList<Product> filteredList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
        for (Product product : arrayList)  {
            if (product.getStore().equals(text))   {
                filteredList.add(product);
            }
        }

        customListAdapter.setFilteredList(filteredList);
        CustomListAdapter adapter = new CustomListAdapter(
                getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
        );
        lv.setAdapter(adapter);

    }

    private void filterList(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
        for (Product product : arrayList)  {
            if (product.getName().toLowerCase().contains(text.toLowerCase()))   {
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty())   {
            //Toast.makeText(getContext().getApplicationContext(), "No product was found", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(getActivity(), "No product was found",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }   else    {
            customListAdapter.setFilteredList(filteredList);
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
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
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("products");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Product(
                            productObject.getString("image"),
                            productObject.getString("name"),
                            productObject.getString("price"),
                            productObject.getString("store")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, arrayList
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


