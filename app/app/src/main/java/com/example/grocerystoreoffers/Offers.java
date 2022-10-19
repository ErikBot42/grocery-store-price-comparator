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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    FloatingActionButton purchaseBtn;
    //Boolean ica=false,coop=false,lidl=false,willys=false;
    Boolean ica,coop,lidl,willys;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public Offers() {
        // Required empty public constructor
    }

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
        coop=false;ica=false;lidl=false;willys=false;
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        arrayList = new ArrayList<>();
        View contentView = inflater.inflate(R.layout.fragment_offers, container, false);
        lv = contentView.findViewById(R.id.listView);
        user = FirebaseAuth.getInstance().getCurrentUser();

        CheckBox catVeg = contentView.findViewById(R.id.vegetarian);
        CheckBox catVegan = contentView.findViewById(R.id.vegan);
        CheckBox catMeat = contentView.findViewById(R.id.meatPoultryFish);
        CheckBox catFruit = contentView.findViewById(R.id.fruitVegetables);



        CheckBox favBox = contentView.findViewById(R.id.favouriteStore);
        CheckBox shopCart = contentView.findViewById(R.id.shoppingCart);
        CheckBox icaBox = contentView.findViewById(R.id.icaStore);
        CheckBox coopBox = contentView.findViewById(R.id.coopStore);
        CheckBox lidlBox = contentView.findViewById(R.id.lidlStore);
        CheckBox willysBox = contentView.findViewById(R.id.willysStore);
        purchaseBtn = contentView.findViewById(R.id.purchaseBtn);
        List<String> shoppingList;

        ArrayList<Product> filteredList = new ArrayList<>();

        purchaseBtn.setVisibility(View.INVISIBLE);

        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("dd/MM-yyyy").format(new Date());
                AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
                alertbox.setTitle("Caution");
                alertbox.setMessage(getResources().getString(R.string.removeShopList));
                alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: Remove shoppinglist from firebase, add to purchasesList
                        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(user != null) {
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null) {
                                            documentReference.update("latestPurchase",FieldValue.arrayUnion(date));
                                            List<String> group = (List<String>) document.get("shoppingList");
                                            for (String prod:group){
                                                documentReference.update("latestPurchase", FieldValue.arrayUnion(prod));
                                                documentReference.update("shoppingList", FieldValue.arrayRemove(prod));
                                                Log.d("TESTREMOVE",prod);
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

                    }
                });
                alertbox.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertbox.show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());

                // set the message to display
                alertbox.setTitle("Choose option");
                alertbox.setItems(new CharSequence[]{"Add to shopping list"}, (dialog, which) -> {
                    // TODO Auto-generated method stub
                    DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if(user != null) {

                        // Name, email address, and profile photo Url
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        Product product = (Product) adapterView.getItemAtPosition(pos);
                                        documentReference.update("shoppingList", FieldValue.arrayUnion(product.getId()));

                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }

                        });
                    }

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
        /*
        favBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getFavstore();
            }
        });
        */
        catFruit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {filterStore();}
                else {
                    getCategory("Fruit");
                }
            }
        });

        favBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){filterStore();}
                else{
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
                }
            }
        });

        shopCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    purchaseBtn.setVisibility(View.INVISIBLE);
                    filterStore();
                } else {
                    purchaseBtn.setVisibility(View.VISIBLE);
                    getCart();
                }
            }
        });
        /*
        icaBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("3");
            }
        });

         */
        icaBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    ica =false;
                    filterStore();
                } else {
                    shopCart.setChecked(false);
                    ica =true;
                    filterStore();
                }
            }
        });
/*
        coopBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterStore("2");
            }
        });

 */
        coopBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    coop=false;
                    filterStore();
                } else {
                    shopCart.setChecked(false);
                    coop=true;
                    filterStore();
                }

            }
        });

        lidlBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b ) {
                    lidl = false;
                    filterStore();
                } else {
                    shopCart.setChecked(false);
                    lidl =true;
                    filterStore();
                }
            }
        });

        willysBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b ) {
                    willys=false;
                    filterStore();
                } else {
                    shopCart.setChecked(false);
                    willys=true;
                    filterStore();
                }
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
                Log.d("EXECTEST","TJENA");
                new ReadJSON().execute("http://130.243.20.190:5000/app/products/");
                //new ReadJSON().execute("https://raw.githubusercontent.com/ErikBot42/grocery-store-price-comparator/main/tmp.json");
            }
        });
        return contentView;
    }

    public void getCategory(String text)   {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("EXECTEST","TJENAFunktion");
                new ReadJSON().execute("http://130.243.20.190:5000/app/products/"+text);
            }
        });
    }

    public void filterStore() {

        if (!coop && !ica && !lidl && !willys) {

            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);

        } else {
            ArrayList<Product> filteredList = new ArrayList<>();
            customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
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

            customListAdapter.setFilteredList(filteredList);
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
            );
            lv.setAdapter(adapter);
        }

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

                        ArrayList<Product> filteredList = new ArrayList<>();
                        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
                        for (String prodId : group) {
                            for (Product product : arrayList) {
                                if (product.getId().equals(prodId)) {
                                    filteredList.add(product);
                                }
                            }
                        }
                        if (filteredList.isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(), "No product was found", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        } else {
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
