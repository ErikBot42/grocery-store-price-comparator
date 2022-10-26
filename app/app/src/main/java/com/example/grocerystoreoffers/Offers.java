package com.example.grocerystoreoffers;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offers extends Fragment {

    ArrayList<Product> arrayList;
    ListView lv;
    ArrayList<CheckBox> checkBoxArrayList;
    TextView errorMess;
    private static Offers instance;
    private SearchView searchView;
    private Toolbar toolbar;
    CustomListAdapter customListAdapter;
    FirebaseFirestore fStore;
    FirebaseUser user;
    FloatingActionButton purchaseBtn;
    Boolean ica,coop,lidl,willys;
    Boolean vegetarian, vegan, meat, fruit, dairy, drink, ice, bread;

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
        vegetarian=false; vegan=false; meat=false; fruit=false; dairy=false; drink=false; ice=false; bread=false;
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        instance =this;
        arrayList = new ArrayList<>();
        checkBoxArrayList = new ArrayList<>();

        View contentView = inflater.inflate(R.layout.fragment_offers, container, false);
        lv = contentView.findViewById(R.id.listView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        errorMess = contentView.findViewById(R.id.errorMessage);
        CheckBox favBox = contentView.findViewById(R.id.favouriteStore);
        CheckBox shopCart = contentView.findViewById(R.id.shoppingCart);
        CheckBox icaBox = contentView.findViewById(R.id.icaStore);
        CheckBox coopBox = contentView.findViewById(R.id.coopStore);
        CheckBox lidlBox = contentView.findViewById(R.id.lidlStore);
        CheckBox willysBox = contentView.findViewById(R.id.willysStore);
        purchaseBtn = contentView.findViewById(R.id.purchaseBtn);
        List<String> shoppingList;

        checkBoxArrayList.add(favBox);
        checkBoxArrayList.add(shopCart);
        checkBoxArrayList.add(icaBox);
        checkBoxArrayList.add(coopBox);
        checkBoxArrayList.add(lidlBox);
        checkBoxArrayList.add(willysBox);

        ArrayList<Product> filteredList = new ArrayList<>();

        final String[] select_qualification = {
                "Select Categories", "Vegetarian", "Vegan", "Meat, Poultry & Fish", "Fruit & Vegetables",
                "Dairy, Cheese & Eggs", "Drink", "Ice Cream, Sweets & Snacks", "Bread & Cookies"};

        Spinner spinner = (Spinner) contentView.findViewById(R.id.spinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(getActivity().getApplicationContext(), 0,
                listVOs);
        spinner.setAdapter(myAdapter);

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
                if(!shopCart.isChecked()) {
                    alertbox.setItems(new CharSequence[]{"Add to shopping list"}, (dialog, which) -> {
                        // TODO Auto-generated method stub
                        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if (user != null) {
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
                }
                else{
                    alertbox.setItems(new CharSequence[]{"Remove from shopping list"}, (dialog, which) -> {
                        // TODO Auto-generated method stub
                        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if (user != null) {
                            // Name, email address, and profile photo Url
                            documentReference.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        Product product = (Product) adapterView.getItemAtPosition(pos);
                                        documentReference.update("shoppingList", FieldValue.arrayRemove(product.getId()));

                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            });
                        }

                    });
                }

                // add a neutral button to the alert box and assign a click listener
                // click listener on the alert box
                alertbox.setNeutralButton("OK", (arg0, arg1) -> {
                    // the button was clicked

                });
                alertbox.show();
            }
        });

        favBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){filterStore();}
                else    {
                    resetCheck(checkBoxArrayList,favBox);
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
                new ReadJSON().execute("http://172.20.10.2:5000/app/products/");
                //new ReadJSON().execute("https://raw.githubusercontent.com/ErikBot42/grocery-store-price-comparator/main/tmp.json");
            }
        });
        Log.d("TESTAR", contentView.toString());
        return contentView;
    }

    public void getCategory(String text)   {

        Log.d("ERRORMYBRAIN", String.valueOf(arrayList.isEmpty()));

        ArrayList<Product> filteredList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
        for (Product product : arrayList) {
            filteredList.remove(product);
            }

        customListAdapter.setFilteredList(filteredList);
        CustomListAdapter adapter = new CustomListAdapter(
                getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
        );
        lv.setAdapter(adapter);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("EXECTEST","TJENAFunktion");
                new ReadJSON().execute("http://172.20.10.2:5000/app/products/"+text);
            }
        });
    }

    public static Offers getInstance()  {
        return instance;
    }

    public void filterStore() {

        lv.setVisibility(View.VISIBLE);
        errorMess.setVisibility(View.GONE);

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
        lv.setVisibility(View.VISIBLE);
        ArrayList<Product> filteredList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
        for (Product product : arrayList)  {
            if (product.getName().toLowerCase().contains(text.toLowerCase()))   {
                errorMess.setVisibility(View.GONE);
                filteredList.add(product);
            }
            else if(product.getId().equals(text)){
                errorMess.setVisibility(View.GONE);
                filteredList.add(product);
            }
        }
        if (filteredList.isEmpty())   {
            errorMess.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
        }   else    {
            customListAdapter.setFilteredList(filteredList);
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
            );
            lv.setAdapter(adapter);
        }
    }

    public void getCart()   {
        lv.setVisibility(View.VISIBLE);
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
                            errorMess.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.INVISIBLE);
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

    public void resetCheck(ArrayList<CheckBox> checkList,CheckBox ignore){
        for (CheckBox i : checkList){
            if(!i.equals(ignore)){
                i.setChecked(false);
            }
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
                            productObject.getString("price_kg"),
                            productObject.getString("store_id"),
                            productObject.getString("category"),
                            productObject.getString("price_l")));
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

    public void filterCategory() {


        if (!vegan && !vegetarian && !meat && !fruit && !dairy && !drink && !ice && !bread) {
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, arrayList
            );
            lv.setAdapter(adapter);
        }
        else {

            ArrayList<Product> filteredList = new ArrayList<>();
            customListAdapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList);
            for (Product product : arrayList) {
                if (vegetarian) {
                    if (product.getCategory().equals("Vegetarian")) {
                        Log.d("FILTERCATEGORY", "VEG FOUND");
                        filteredList.add(product);
                    }
                }
                if (vegan) {
                    if (product.getCategory().equals("Vegan")) {
                        Log.d("FILTERCATEGORY", "VEGAN FOUND");
                        filteredList.add(product);
                    }
                }
                if (meat) {
                    if (product.getCategory().equals("Meat")) {
                        Log.d("FILTERCATEGORY", "MEAT FOUND");
                        filteredList.add(product);
                    }
                }
                if (fruit) {
                    if (product.getCategory().equals("Fruit")) {
                        Log.d("FILTERCATEGORY", "FRUIT FOUND");
                        filteredList.add(product);
                    }
                }
                if (dairy) {
                    if (product.getCategory().equals("Dairy")) {
                        Log.d("FILTERCATEGORY", "DAIRY FOUND");
                        filteredList.add(product);
                    }
                }
                if (drink) {
                    if (product.getCategory().equals("Drink")) {
                        Log.d("FILTERCATEGORY", "DRINK FOUND");
                        filteredList.add(product);
                    }
                }
                if (ice) {
                    if (product.getCategory().equals("Sweets")) {
                        Log.d("FILTERCATEGORY", "DAIRY FOUND");
                        filteredList.add(product);
                    }
                }
                if (bread) {
                    if (product.getCategory().equals("Bread")) {
                        Log.d("FILTERCATEGORY", "DRINK FOUND");
                        filteredList.add(product);
                    }
                }
            }
        /*
        if (filteredList.isEmpty())   {
            Toast toast = Toast.makeText(getActivity(), "No product was found",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }   else    {

        }

         */
            customListAdapter.setFilteredList(filteredList);
            CustomListAdapter adapter = new CustomListAdapter(
                    getActivity().getApplicationContext(), R.layout.custom_list_layout, filteredList
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

