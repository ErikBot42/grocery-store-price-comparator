package com.example.grocerystoreoffers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    EditText username;
    EditText password;

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

        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
        /*
        if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {

            System.out.println("Correct login\n");
            System.out.println(username);
            System.out.println(password);

        } else {
            System.out.println("Wrong input");
        }
        */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Check if user is logged in, then get to profile page, else goto login/register page

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button btnLogin = view.findViewById(R.id.btn_login);
        Button register_btn = view.findViewById(R.id.btn_register);

        username = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnLogin.setVisibility(View.INVISIBLE);

                final String mail = username.getText().toString();
                final String password1 = password.getText().toString();

                if (mail.isEmpty() || password1.isEmpty()) {
                    printToast("Please Verify All Field");
                    btnLogin.setVisibility(View.VISIBLE);
                }
                else
                {
                    signIn(mail,password1);
                }
            }
        });
        // Login
        /*
        login_btn.setOnClickListener(view1 -> {
            //TODO: Check whether email is unused, if unused, prompt to register
            System.out.println("Email: " + username.getText().toString() + "\n");
            System.out.println("Password: " + password.getText().toString() + "\n");
        });

         */

        register_btn.setOnClickListener(view1 -> {
            System.out.println("Register button clicked from profFrag");
            replaceFragment(new RegisterFragment());
        });

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.remember);
        //checkBox.setChecked(checkPasswordExist()); Kolla JSON token
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    //TODO: Get JSON-token with extended time
                    printToast("Checked");
                }
                else {
                    printToast("Unchecked");
                }
            }
        });

        return view;
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    printToast("CORRECT LOGIN");
                    /*
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();

                     */

                }
                else {
                    printToast("INCORRECT LOGIN");
                    /*
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);

                     */
                }


            }
        });
    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void printToast(String message){
        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

}