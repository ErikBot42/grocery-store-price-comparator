package com.example.grocerystoreoffers;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;
import java.util.Objects;

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
    FirebaseFirestore fStore;
    FirebaseUser user;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrayList = new ArrayList<>();
        View contentView = inflater.inflate(R.layout.fragment_offers, container, false);
        lv = contentView.findViewById(R.id.listView);

        CheckBox favBox = contentView.findViewById(R.id.favouriteStore);
        CheckBox shopCart = contentView.findViewById(R.id.shoppingCart);
        CheckBox icaBox = contentView.findViewById(R.id.icaStore);
        CheckBox coopBox = contentView.findViewById(R.id.coopStore);
        CheckBox lidlBox = contentView.findViewById(R.id.lidlStore);
        CheckBox willysBox = contentView.findViewById(R.id.willysStore);
        List<String> shoppingList;
        ArrayList<Product> filteredList = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());

                // set the message to display
                alertbox.setTitle("Choose option");
                alertbox.setItems(new CharSequence[]{"Add to shopping list"}, (dialog, which) -> {
                    // TODO Auto-generated method stub

                });

                // add a neutral button to the alert box and assign a click listener
                alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                    // click listener on the alert box
                    public void onClick(DialogInterface arg0, int arg1) {
                        // the button was clicked

                    }
                });
                alertbox.show();
            }
        });

        favBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFavstore();
            }
        });

        shopCart.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) { getCart(); }
        }));

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
                new ReadJSON().execute("http://193.10.238.209:5000/app/products/");
            }
        });
        return contentView;
    }

    public void filterStore(String text)  {

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
            else if(product.getId().equals(text)){
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty())   {
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

    public void getCart()   {
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        List<String> group = (List<String>) document.get("shoppingList");
                        Log.d("minLista", String.valueOf(group));
                        ArrayList<Product> filteredList = new ArrayList<>();
                        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
                        for (String prodId : group) {
                            for (Product product : arrayList) {
                                if (product.getId().equals(prodId)) {
                                    filteredList.add(product);
                                }
                            }
                        }
                        if (filteredList.isEmpty())   {
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
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });



    }

    public void getFavstore(){
        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String favStore = document.getString("Favorite store");
                        //To prevent null-pointer exeptions
                        switch (Objects.requireNonNull(favStore)){
                            case "ICA":
                                filterStore("3");
                                break;
                            case "Coop":
                                filterStore("2");
                                break;
                            case "Willys":
                                filterStore("4");
                                break;
                            case "LIDL":
                                filterStore("1");
                                break;
                            default:
                                break;
                        }
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
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
                content = content.substring(1);
                Log.d("content",content);

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
