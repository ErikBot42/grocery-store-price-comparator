package com.example.grocerystoreoffers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link resetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class resetPassword extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mAuth;
    Button resetPass;
    EditText emailBox;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public resetPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment resetPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static resetPassword newInstance(String param1, String param2) {
        resetPassword fragment = new resetPassword();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        resetPass = view.findViewById(R.id.btn_reset);
        emailBox = view.findViewById(R.id.resetEmail);


        resetPass.setOnClickListener(v -> {
            final String email = emailBox.getText().toString();
            Log.d("EMAILCHECK", email);
            if(email.isEmpty()){
                printToast(getResources().getString(R.string.enterEmail));
            }
            else{
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        printToast(getResources().getString(R.string.emailSuccess));
                        replaceFragment(new LoginFragment());
                    }
                    else{
                        printToast("Failed to send email");
                    }
                });

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void printToast(String message){
        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}