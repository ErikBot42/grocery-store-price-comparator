package com.example.grocerystoreoffers;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/*
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

*/

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import modal.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }
    EditText fullName,email,password,rePassword,telephone;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference ref;
    String userID;
    private String TAG;
    private Button regBtn;
    private ProgressBar loadingProgress;

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("server/saving-data/fireblog");
        regBtn = view.findViewById(R.id.btn_register);
        Button backToLogin_btn = view.findViewById(R.id.btn_login);
        fullName = view.findViewById(R.id.et_name);
        password = view.findViewById(R.id.et_password);
        email = view.findViewById(R.id.et_email);
        rePassword = view.findViewById(R.id.et_repassword);
        telephone = view.findViewById(R.id.et_telephone);
        loadingProgress = view.findViewById(R.id.regProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);
        Spinner spinner = view.findViewById(R.id.favStores);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.stores_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgress.setVisibility(View.VISIBLE);
                //regBtn.setVisibility(View.INVISIBLE);
                final String email1 = email.getText().toString();
                final String password1 = password.getText().toString();
                final String password2 = rePassword.getText().toString();
                final String name = fullName.getText().toString();
                final String telephone1 = telephone.getText().toString();
                final String favStore = spinner.getSelectedItem().toString();


                if (email1.isEmpty() || name.isEmpty() || password1.isEmpty() || !password2.equals(password2)) {


                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    printToast("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);


                } else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid

                    CreateUserAccount(email1, name, password1,telephone1,favStore);
                }
            }
        });
        // Change to login fragment
        backToLogin_btn.setOnClickListener(view1 -> {
            replaceFragment(new LoginFragment());

        });
        return view;
    }

    private void CreateUserAccount(String email, String name, String password,String telephone,String favStore) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // user account created successfully
                        printToast("Account created");
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("user_profile").document(userID);


                        Map<String, Object> user = new HashMap<>();
                        user.put("LIDL",false);
                        user.put("COOP",false);
                        user.put("ICA",false);
                        user.put("Willys",false);
                        user.put(favStore,true);
                        user.put("Name", name);
                        user.put("Email", email);
                        user.put("Password", password);
                        user.put("Telephone",telephone);
                        //user.put("Favorite store", favStore);
                        replaceFragment(new LoginFragment());
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });

                    } else {
                        // account creation failed
                        printToast("account creation failed" + task.getException().getMessage());
                        regBtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void printToast(String message){
        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}


