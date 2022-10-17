package com.example.grocerystoreoffers;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseUser user;
    TextView fullname_field,username_field,favoritestore_field;
    Button button2,updateBtn;
    ListView storeList;
    FirebaseFirestore fStore;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fullname_field = view.findViewById(R.id.fullname_field);
        username_field = view.findViewById(R.id.username_field);
        favoritestore_field = view.findViewById(R.id.favoritestore_field);
        button2 = view.findViewById(R.id.button2);
        updateBtn = view.findViewById(R.id.updateButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        MaterialCardView icaCard = view.findViewById(R.id.icaCard);
        MaterialCardView coopCard = view.findViewById(R.id.coopCard);
        MaterialCardView willysCard = view.findViewById(R.id.willysCard);
        MaterialCardView lidlCard = view.findViewById(R.id.lidlCard);


        updateBtn.setVisibility(View.INVISIBLE);




        //TODO: Check if user is logged in, then get to profile page, else goto login/register page
        DocumentReference documentReference = fStore.collection("user_profile").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(user != null) {
            // Name, email address, and profile photo Url
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            String name = document.getString("Name");
                            /*
                            String favStore = document.getString("Favorite store");
                            favoritestore_field.setText("Favorite store: " + favStore);

                             */
                            if(document.getBoolean("COOP").equals(true)){
                                coopCard.toggle();
                                Log.d("CARDSTATUS","COOP: "+Boolean.valueOf(coopCard.isChecked()).toString());
                            }
                            if(document.getBoolean("ICA").equals(true)){
                                icaCard.toggle();
                                Log.d("CARDSTATUS","ICA: "+Boolean.valueOf(icaCard.isChecked()).toString());
                            }

                            if(document.getBoolean("Willys").equals(true)){
                                willysCard.toggle();
                                Log.d("CARDSTATUS","WILLYS: "+Boolean.valueOf(willysCard.isChecked()).toString());
                            }
                            if(document.getBoolean("LIDL").equals(true)){
                                lidlCard.toggle();
                                Log.d("CARDSTATUS","LIDL: "+Boolean.valueOf(lidlCard.isChecked()).toString());
                            }
                            fullname_field.setText(name);
                            Log.i("LOGGER","First "+document.getString("first"));
                            Log.i("LOGGER","Last "+document.getString("last"));
                            Log.i("LOGGER","Born "+document.getString("born"));
                        } else {
                            Log.d("LOGGER", "No such document");
                        }
                    } else {
                        Log.d("LOGGER", "get failed with ", task.getException());
                    }
                }

            });
            //String name = user.getDisplayName();
            String email = user.getEmail();
            username_field.setText("Email: "+email);
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        icaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setVisibility(View.VISIBLE);
                icaCard.setChecked(!icaCard.isChecked());
            }
        });
        coopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setVisibility(View.VISIBLE);
                coopCard.setChecked(!coopCard.isChecked());
            }
        });
        willysCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setVisibility(View.VISIBLE);
                willysCard.setChecked(!willysCard.isChecked());
            }
        });
        lidlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setVisibility(View.VISIBLE);
                lidlCard.setChecked(!lidlCard.isChecked());
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lidlCard.isChecked()){documentReference.update("LIDL",true);}
                else{documentReference.update("LIDL",false);}
                if(coopCard.isChecked()){documentReference.update("COOP",true);}
                else{documentReference.update("COOP",false);}
                if(icaCard.isChecked()){documentReference.update("ICA",true);}
                else{documentReference.update("ICA",false);}
                if(willysCard.isChecked()){documentReference.update("Willys",true);}
                else{documentReference.update("Willys",false);}
                Button button = (Button) view;
                button.setVisibility(View.GONE);
            }

        });
        // Inflate the layout for this fragment
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                replaceFragment(new LoginFragment());
            }
        });

        return view;
    }



    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void printToast(String message){
        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

}