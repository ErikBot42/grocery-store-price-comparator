package com.example.grocerystoreoffers;

import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;


import androidx.fragment.app.FragmentTransaction;







import android.app.AlarmManager;


import android.app.PendingIntent;


import android.content.Context;


import android.content.Intent;


import android.content.SharedPreferences;


import android.content.res.Configuration;


import android.content.res.Resources;


import android.os.Build;


import android.os.Bundle;


import android.util.DisplayMetrics;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.Button;


import android.widget.EditText;


import android.widget.Toast;







import com.example.grocerystoreoffers.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    EditText usernameEdt, passwordEdt;
    Resources resources;
    Context context;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadData();
        String loadedLang = text;
        setAppLocale(text);
        setTitle(R.string.app_name);
        super.onCreate(savedInstanceState);
        getApplicationContext();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(binding.getRoot());
        replaceFragment(firebaseAuth.getCurrentUser() != null ? new HomeFragment() : new LoginFragment());
        //usernameEdt =(EditText) findViewById(R.id.et_email);
        //passwordEdt =(EditText) findViewById(R.id.et_password);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    if(firebaseAuth.getCurrentUser() != null){
                        replaceFragment(new HomeFragment());
                        break;
                    }
                    else{
                        replaceFragment(new LoginFragment());
                    }
                    break;
                case R.id.profile:
                    if(firebaseAuth.getCurrentUser() != null){
                        replaceFragment(new ProfileFragment());
                        break;
                    }
                    else{
                        replaceFragment(new LoginFragment());
                    }
                    break;
                case R.id.settings:
                    if(firebaseAuth.getCurrentUser() != null){
                        replaceFragment(new Offers());
                        break;
                    }
                    else{
                        replaceFragment(new LoginFragment());
                    }
                    break;
                case R.id.map:
                    if(firebaseAuth.getCurrentUser() != null){
                        replaceFragment(new NearbyStoresFragment());
                        break;
                    }
                    else{
                        replaceFragment(new LoginFragment());
                    }
                    break;
            }
            return true;
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.langbtn, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                saveData("sv");
                return true;
            case R.id.engLang:
                saveData("en");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }
    public void saveData(String lang)   {

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, lang);
        editor.commit();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
        replaceFragment(new ProfileFragment());
        this.recreate();
    }

    private void setAppLocale(String localeCode)    {

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }else{
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }
}
