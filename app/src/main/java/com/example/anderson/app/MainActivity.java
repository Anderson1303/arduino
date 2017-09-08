package com.example.anderson.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.example.androidbtcontrol.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste);
        iniciaFragment();
    }

     public void iniciaFragment(){
         FragmentVelocidade velocidade = new FragmentVelocidade();
         FragmentManager fm = getSupportFragmentManager();
         FragmentTransaction ft = fm.beginTransaction();
         ft.replace(R.id.container,velocidade);
         ft.commit();
     }
}