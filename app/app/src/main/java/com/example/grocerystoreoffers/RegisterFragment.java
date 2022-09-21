package com.example.grocerystoreoffers;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }
    EditText fullName,email,password,rePassword;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        Button register_btn = view.findViewById(R.id.btn_register);
        Button backToLogin_btn = view.findViewById(R.id.btn_login);

        fullName = view.findViewById(R.id.et_name);
        password = view.findViewById(R.id.et_password);
        //TODO: Check if email is taken
        email = view.findViewById(R.id.et_email);
        rePassword = view.findViewById(R.id.et_repassword);

        register_btn.setOnClickListener(view1 -> {
            if(!validateEmail(email.getText().toString())){
                printToast("Invalid email");
            }else {
                System.out.println("Email: " + email.getText().toString() + "\n");
            }
            System.out.println("Full Name: " + fullName.getText().toString() + "\n");
            if(passwordSameCheck(password.getText().toString(),rePassword.getText().toString())) {
                System.out.println("Password: " + password.getText().toString() + "\n");
                System.out.println("RePassword: " + rePassword.getText().toString() + "\n");
            }else{
                printToast("Passwords does not match");
            }
        });
        // Change to login fragment
        backToLogin_btn.setOnClickListener(view1 -> {
            replaceFragment(new ProfileFragment());

        });
        return view;
    }
    // Function to check if email is in correct format (abc(.abc)@abc.abc)
    private boolean validateEmail(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean passwordSameCheck(CharSequence password, CharSequence rePassword){
        return password.equals(rePassword);
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